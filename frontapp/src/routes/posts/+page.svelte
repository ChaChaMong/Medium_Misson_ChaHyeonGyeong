<script>
	let posts = $state([]);
	$effect(() => {
		fetch('http://localhost:8090/api/v1/posts')
			.then((response) => response.json())
			.then((json) => (posts = json));
	});
</script>

<table>
	<thead>
		<tr>
			<th>제목</th>
			<th>내용</th>
		</tr>
	</thead>
	<tbody>
		{#each posts.data as post (post.id)}
			<tr>
				<td>{post.title}</td>
				<td>{post.body}</td>
			</tr>
		{/each}
	</tbody>
</table>
