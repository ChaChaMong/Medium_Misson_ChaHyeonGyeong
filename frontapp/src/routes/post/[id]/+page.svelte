<script lang="ts">
	import rq from '$lib/rq/rq.svelte';
	import type { components } from '$lib/types/api/v1/schema';
	import { page } from '$app/stores';
	import { onMount } from 'svelte';
	import { formatDate } from '$lib/utils/dateUtils';

	onMount(() => {
		$page.params.id;
	});

	let post: components['schemas']['PostDto'] | null = $state(null);

	rq.effect(async () => {
		const { data, error } = await rq.apiEndPoints().GET('/api/v1/posts/{id}', {
			params: {
				path: {
					id: parseInt($page.params.id)
				}
			}
		});

		if (data) {
			post = data.data;
		} else {
			rq.goBack();
			rq.msgError(error.msg);
		}
	});

	async function confirmDelete() {
		if (confirm('정말로 삭제하시겠습니까?')) {
			const { data, error } = await rq.apiEndPoints().DELETE('/api/v1/posts/{id}', {
				params: {
					path: {
						id: parseInt($page.params.id)
					}
				}
			});

			if (data) {
				rq.msgInfo(data.msg);
				rq.goto('/post/myList');
			} else {
				rq.msgError(error.msg);
			}
		}
	}
</script>

<svelte:head>
	<title>게시글 내용</title>
	<meta name="description" content="게시글 내용" />
</svelte:head>

<h1 class="font-bold mb-2">
	<i class="fa-regular fa-newspaper"></i> 게시글 내용
</h1>
<hr />

{#if post}
	<div class="px-4 py-10">
		<div class="flex flex-col max-w-6xl mx-auto md:flex-row">
			<div class="w-full mr-8 md:w-1/3">
				<p class="badge badge-outline">{post.id}</p>
				<p class={post.published ? 'badge badge-success' : 'badge badge-warning'}>
					{post.published ? '공개' : '비공개'}
				</p>
				<h2 class="mt-2 text-3xl font-extrabold leading-9" style="word-break: break-all;">
					{#if post.paid}
						<i class="fa-solid fa-coins" style="color:orange;"></i>
					{/if}
					{post.title}
				</h2>
				<p class="mt-4">작성자 : {post.authorName}</p>
				<p class="mt-2">등록 일시 : {formatDate(new Date(post.createDate))}</p>
				<p class="mb-2">수정 일시 : {formatDate(new Date(post.modifyDate))}</p>
			</div>
			<dl class="w-full md:w-2/3 whitespace-pre-line" style="word-break: break-all;">
				{#if post.paid && !rq.member.paid}
					<p style="color:orange; font-weight:bold; font-size:large">
						[이 글은 유료멤버십전용 입니다.]
					</p>
				{:else}
					{post.body}
				{/if}
			</dl>
		</div>
	</div>

	<hr class="mt-2" />

	<div class="mt-2 flex gap-2 justify-center">
		{#if post.permission.canModify}
			<a href={`/post/${post.id}/modify`} class="btn btn-warning btn-sm">글 수정</a>
		{/if}
		{#if post.permission.canDelete}
			<button class="btn btn-error btn-sm" on:click={confirmDelete}>글 삭제</button>
		{/if}
	</div>
{/if}
