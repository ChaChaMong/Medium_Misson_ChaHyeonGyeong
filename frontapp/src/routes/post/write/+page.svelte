<script lang="ts">
	import rq from '$lib/rq/rq.svelte';
	import type { components } from '$lib/types/api/v1/schema';

	let post: components['schemas']['PostRequestDto'] = {
		title: '',
		body: '',
		published: false
	};

	let titleInput: HTMLElement;
	let bodyInput: HTMLElement;

	async function submitWriteForm(this: HTMLFormElement) {
		const form: HTMLFormElement = this;
		post.title = post.title.trim();
		if (post.title.length === 0) {
			rq.msgError('제목을 입력해주세요.');
			titleInput.focus();
			return;
		}
		post.body = post.body.trim();
		if (post.body.length === 0) {
			rq.msgError('내용을 입력해주세요.');
			bodyInput.focus();
			return;
		}
		const { data, error } = await rq.apiEndPoints().POST('/api/v1/posts', {
			body: post
		});

		if (data) {
			rq.msgInfo(data.msg);
			rq.goto(`/post/${data.data.id}`);
		} else {
			rq.msgError(error.msg);
		}
	}
</script>

<svelte:head>
	<title>게시글 작성</title>
	<meta name="description" content="게시글 작성" />
</svelte:head>

<h1 class="font-bold mb-2">
	<i class="fa-solid fa-pen"></i> 게시글 작성
</h1>

<form class="flex flex-col gap-4" on:submit|preventDefault={submitWriteForm}>
	<div class="form-control">
		<label class="label" for="title-input">
			<span class="label-text">제목</span>
		</label>
		<input
			bind:this={titleInput}
			name="title"
			id="title-input"
			type="text"
			bind:value={post.title}
			placeholder="제목을 입력해주세요."
			class="input input-bordered"
		/>
	</div>

	<div class="form-control">
		<label class="label" for="body-input">
			<span class="label-text">내용</span>
		</label>
		<textarea
			bind:this={bodyInput}
			name="body"
			id="body-input"
			bind:value={post.body}
			maxlength="10000"
			rows="5"
			placeholder="내용을 입력해주세요."
			class="textarea textarea-bordered"
		></textarea>
	</div>

	<div class="form-control">
		<label class="label" for="published-input">
			<div style="display: flex; align-items: center;">
				<span class="label-text" style="margin-right: 10px;">글 공개</span>
				<input
					name="published"
					id="published-input"
					type="checkbox"
					bind:checked={post.published}
					class="toggle toggle-lg"
				/>
			</div>
		</label>
	</div>

	<button class="btn btn-primary btn-block" type="submit">
		<i class="fa-solid fa-pen"></i> 게시글 작성
	</button>
</form>
