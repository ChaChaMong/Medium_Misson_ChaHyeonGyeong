<script lang="ts">
	export let postPage: any;
</script>

{#if postPage != null}
	<div class="w-full flex justify-center mt-1 {postPage.totalElementsCount > 1 ? 'join' : ''}">
		{#if postPage.hasPrevious}
			<a class="join-item btn btn-sm" href={`?page=${postPage.number - 1}`}>
				<span>이전</span>
			</a>
		{/if}

		{#each Array.from({ length: postPage.totalPagesCount }) as _, index}
			{#if index >= postPage.number - 3 && index <= postPage.number + 3}
				<a
					class="join-item btn btn-sm {index === postPage.number ? 'btn-active' : ''}"
					href={`?page=${index}`}
				>
					{index + 1}
				</a>
			{/if}
		{/each}

		{#if postPage.hasNext}
			<a class="join-item btn btn-sm" href={`?page=${postPage.number + 1}`}>
				<span>다음</span>
			</a>
		{/if}
	</div>
{/if}
