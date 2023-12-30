<script lang="ts">
	import rq from '$lib/rq/rq.svelte';

	async function submitLoginForm(this: HTMLFormElement) {
		const form: HTMLFormElement = this;
		form.username.value = form.username.value.trim();
		if (form.username.value.length === 0) {
			rq.msgError('아이디를 입력해주세요.');
			form.username.focus();
			return;
		}
		form.password.value = form.password.value.trim();
		if (form.password.value.length === 0) {
			rq.msgError('비밀번호를 입력해주세요');
			form.password.focus();
			return;
		}
		const { data, error } = await rq.apiEndPoints().POST('/api/v1/members/login', {
			body: {
				username: form.username.value,
				password: form.password.value
			}
		});
		if (data) {
			rq.msgInfo(data.msg);
			rq.setLogined(data.data.item);
			rq.goto('/');
		} else if (error) {
			rq.msgError(error.msg);
		}
	}
</script>

<svelte:head>
	<title>로그인</title>
	<meta name="description" content="로그인" />
</svelte:head>

<h1 class="font-bold mb-2"><i class="fa-solid fa-right-to-bracket"></i> 로그인</h1>

<form class="flex flex-col gap-4" on:submit|preventDefault={submitLoginForm}>
	<div class="form-control">
		<label class="label" for="username-input">
			<span class="label-text">아이디</span>
		</label>
		<input
			type="text"
			name="username"
			id="username-input"
			placeholder="아이디를 입력해주세요."
			class="input input-bordered"
		/>
	</div>

	<div class="form-control">
		<label class="label" for="password-input">
			<span class="label-text">비밀번호</span>
		</label>
		<input
			type="password"
			name="password"
			id="password-input"
			placeholder="비밀번호를 입력해주세요."
			class="input input-bordered"
		/>
	</div>

	<button class="btn btn-primary btn-block" type="submit">
		<i class="fa-solid fa-right-to-bracket"></i> 로그인
	</button>
</form>
