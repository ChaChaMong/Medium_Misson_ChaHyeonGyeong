<script lang="ts">
	import rq from '$lib/rq/rq.svelte';

	async function submitJoinForm(this: HTMLFormElement) {
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
		form.passwordConfirm.value = form.passwordConfirm.value.trim();
		if (form.passwordConfirm.value.length === 0) {
			rq.msgError('비밀번호 확인을 입력해주세요');
			form.passwordConfirm.focus();
			return;
		}
		const { data, error } = await rq.apiEndPoints().POST('/api/v1/members/join', {
			body: {
				username: form.username.value,
				password: form.password.value,
				passwordConfirm: form.passwordConfirm.value
			}
		});
		if (data) {
			rq.msgInfo(data.msg);
			rq.goto('/member/login');
		} else if (error) {
			rq.msgError(error.msg);
		}
	}
</script>

<svelte:head>
	<title>회원가입</title>
	<meta name="description" content="회원가입" />
</svelte:head>

<h1 class="font-bold mb-2"><i class="fa-solid fa-user-plus"></i> 회원가입</h1>

<form class="flex flex-col gap-4" on:submit|preventDefault={submitJoinForm}>
	<div class="form-control">
		<label class="label" for="username-input">
			<span class="label-text">아이디</span>
		</label>
		<input
			type="text"
			name="username"
			id="username-input"
			autocomplete="off"
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

	<div class="form-control">
		<label class="label" for="passwordConfirm-input">
			<span class="label-text">비밀번호 확인</span>
		</label>
		<input
			type="password"
			name="passwordConfirm"
			id="passwordConfirm-input"
			placeholder="비밀번호 확인을 입력해주세요."
			class="input input-bordered"
		/>
	</div>

	<button class="btn btn-primary btn-block" type="submit">
		<i class="fa-solid fa-user-plus"></i> 회원가입
	</button>
</form>
