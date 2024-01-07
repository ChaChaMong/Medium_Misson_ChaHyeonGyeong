export let formatDate = (date: Date): string => {
	let year = date.getFullYear().toString().slice(-2);
	let month = (date.getMonth() + 1).toString().padStart(2, '0');
	let day = date.getDate().toString().padStart(2, '0');
	let hours = date.getHours().toString().padStart(2, '0');
	let minutes = date.getMinutes().toString().padStart(2, '0');
	let seconds = date.getSeconds().toString().padStart(2, '0');

	return `${year}.${month}.${day} ${hours}:${minutes}:${seconds}`;
};
