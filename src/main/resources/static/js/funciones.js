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

function actualizarVisibilidadRegistroEmpleado() {
    const select = document.getElementById('selectRol');
    const rol = select.options[select.selectedIndex].text;

    // Contenedores
    const contEmpresa = document.getElementById('containerEmpresa');
    const contSede = document.getElementById('containerSede');
    const contInstalacion = document.getElementById('containerInstalacion');

    // 1. Ocultar todo y quitar required
    [contEmpresa, contSede, contInstalacion].forEach(div => {
        div.classList.add('d-none');
        div.querySelector('select').removeAttribute('required');
    });

    // 2. Mostrar según el Rol y activar required
    if (rol === 'USUARIO_EMPRESA') {
        contEmpresa.classList.remove('d-none');
        contEmpresa.querySelector('select').setAttribute('required', 'required');
    } else if (rol === 'USUARIO_SEDE') {
        contSede.classList.remove('d-none');
        contSede.querySelector('select').setAttribute('required', 'required');
    } else if (rol === 'USUARIO_INSTALACION') {
        contInstalacion.classList.remove('d-none');
        contInstalacion.querySelector('select').setAttribute('required', 'required');
    }
}