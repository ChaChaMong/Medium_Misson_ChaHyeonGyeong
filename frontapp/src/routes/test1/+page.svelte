<script>
	// 사용자 입력을 위한 상태
	let username = $state('');
	// 리포지토리 목록을 저장할 상태
	let repositories = $state([]);
	// 사용자 이름을 입력하고 '데이터 가져오기' 버튼을 누를 때 실행할 함수
	async function fetchRepositories() {
		const response = await fetch(`https://api.github.com/users/${username}/repos?per_page=10`);
		repositories = await response.json();
	}
</script>

<svelte:head>
	<title>TEST 1</title>
	<meta name="description" content="테스트 데이터 가져오기"/>
</svelte:head>

<div class="p-5">
	<form on:submit|preventDefault={fetchRepositories}>
		<input type="text" bind:value={username} placeholder="GitHub 사용자 이름 입력"/>
		<button type="submit">데이터 가져오기</button>
	</form>
	{#if username}
		<h1>{username}'s repositories</h1>
		<ul>
			{#each repositories as repo}
				<li>
					- {repo.name}
				</li> <!-- 여기서 repo 객체의 다른 속성도 사용할 수 있습니다 -->
			{/each}
		</ul>
	{/if}
</div>