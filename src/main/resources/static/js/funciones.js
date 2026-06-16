/**
 * @param {string} id - Valor seleccionado
 * @param {string} url - Endpoint
 * @param {string} targetId - Selector a rellenar
 * @param {string} tipo - 'comunidad' o 'provincia' (para el filtro de municipios)
 */
async function cargarDatos(id, url, targetId, tipo = null) {
	const targetSelect = document.getElementById(targetId);
	targetSelect.innerHTML = '<option value="">Seleccione...</option>';

	if (!id) return;

	// Construir URL con parámetro extra si es necesario
	const fullUrl = tipo ? `${url}?padreId=${id}&tipo=${tipo}` : `${url}?padreId=${id}`;

	const response = await fetch(fullUrl);
	const data = await response.json();

	data.forEach(item => {
		const option = document.createElement('option');
		option.value = item.id;
		option.text = item.nombre;
		targetSelect.appendChild(option);
	});
}

function resetSelect(id) {
	document.getElementById(id).innerHTML = '<option value="">Seleccione...</option>';
}