<script lang="ts">
	import rq from '$lib/rq/rq.svelte';
	import type { components } from '$lib/types/api/v1/schema';
	import { page } from '$app/stores';

	const urlUsername = $page.params.username;

	let posts: components['schemas']['PostDto'][] = $state([]);
	rq.effect(async () => {
		const { data } = await rq.apiEndPoints().GET('/api/v1/b/{username}', {
			params: {
				path: {
					username: urlUsername
				}
			}
		});

		if (data) {
			posts = data.data;
		}
	});
</script>

<svelte:head>
	<title>{urlUsername} 게시글 목록</title>
	<meta name="description" content="{urlUsername} 게시글 목록" />
</svelte:head>

<h1 class="font-bold mb-2">
	<i class="fa-solid fa-list"></i>
	{urlUsername} 게시글 목록
</h1>

<div class="overflow-x-auto">
	<div class="flex flex-col max-w-6xl mx-auto md:flex-row">
		<table class="table">
			<!-- head -->
			<thead>
				<tr>
					<th class="text-center"></th>
					<th>제목</th>
					<th>작성자</th>
				</tr>
			</thead>
			<tbody>
				{#each posts as post}
					<!-- row -->
					<tr>
						<td class="text-center" style="white-space: nowrap; width: 1%;">
							<span
								style="font-size: 12px; font-weight: bold;"
								class={post.published ? 'badge badge-success' : 'badge badge-warning'}
							>
								{post.published ? '공개' : '비공개'}
							</span>
						</td>
						<td>
							<a href={`post/${post.id}`}>
								<span class="badge badge-outline">{post.id}</span>
								<span style="word-break: break-all;">{post.title}</span>
							</a>
						</td>
						<td
							style="cursor: pointer; white-space: nowrap; width: 1%;"
							on:click={() => (location.href = `/b/${post.authorName}`)}
						>
							<span>{post.authorName}</span>
						</td>
					</tr>
				{/each}
			</tbody>
		</table>
	</div>
</div>
