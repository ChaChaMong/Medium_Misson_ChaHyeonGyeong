<script lang="ts">
	import rq from '$lib/rq/rq.svelte';
	import '$lib/app.css';
	import { onMount } from 'svelte';

	const { children } = $props();

	onMount(() => {
		rq.initAuth();
	});
</script>

<nav class="navbar bg-base-100">
	<div class="navbar-start">
		<div class="dropdown">
			<button tabindex="0" class="btn btn-ghost btn-circle">
				<svg
					xmlns="http://www.w3.org/2000/svg"
					class="h-5 w-5"
					fill="none"
					viewBox="0 0 24 24"
					stroke="currentColor"
				>
					<path
						stroke-linecap="round"
						stroke-linejoin="round"
						stroke-width="2"
						d="M4 6h16M4 12h16M4 18h7"
					/>
				</svg>
			</button>
			<ul class="menu menu-sm dropdown-content mt-3 z-[1] p-2 shadow bg-base-100 rounded-box w-52">
				<li><a href="/post/list"><i class="fa-solid fa-list"></i> 게시물 목록</a></li>
				{#if rq.isLogin()}
					<li><a href="/post/myList"><i class="fa-solid fa-clipboard-list"></i> 내 글 목록</a></li>
					<li><a href="/post/write"><i class="fa-solid fa-pen"></i> 게시물 작성</a></li>
					<li>
						<button on:click={() => rq.logout()}
							><i class="fa-solid fa-right-from-bracket"></i> 로그아웃</button
						>
					</li>
				{:else}
					<li><a href="/member/login"><i class="fa-solid fa-right-to-bracket"></i> 로그인</a></li>
					<li><a href="/member/join"><i class="fa-solid fa-user-plus"></i> 회원가입</a></li>
				{/if}
			</ul>
		</div>
	</div>
	<div class="navbar-center">
		<a href="/" class="btn btn-ghost text-xm">MEDIUM</a>
	</div>
	<div class="navbar-end">
		{#if rq.isLogin()}
			<a class="btn btn-ghost text-xm" href="/member/me">
				<span>{rq.member.username}</span>
			</a>
		{/if}
	</div>
</nav>

<main class="p-4">
	{@render children()}
</main>
