window.addEventListener('load', function() {
    var script = document.querySelector('script[src^="/js/perfil.js"]'); // obtiene el elemento script que tiene el atributo src que empieza con "/js/perfil.js"
    var url = script.src; // obtiene el valor del atributo src
    var userName = url.split('?')[1]; // obtiene la parte después del signo de interrogación
    verPerfil(userName);
});

//get img
document.getElementById('postFotoForm').addEventListener('submit', async function(e) {
    e.preventDefault();
    let api_url = "http://localhost:8080";
    let formData = new FormData(document.getElementById('postFotoForm'));
    let img = document.getElementById('perf-foto');
    console.log('intento subir imagen');
    try {
        const res = await fetch(`${api_url}/perfil/guardarFoto`, {
            method: 'POST',
            body: formData
        })
        if (res.ok) {
            console.log("Image uploaded successfully");
            img.src = '/img/' + res.body;
            window.location.reload();

            // Handle any further actions if needed
        } else {
            console.log("Image upload failed");
            // Handle the error condition
        }
    } catch (err) {
        console.error(err);
    }
})

async function verPerfil(correo) {
    let api_url = "http://localhost:8080";

    let footerModal = document.getElementById("footerModal");

    await fetch(`${api_url}/perfil/${correo}`)
            .then((response) => response.json())
            .then((perfil) => {
                let imagen = document.getElementById('perf-foto');
                imagen.src = '/img/' + perfil.foto;
                imagen.alt = '/img/' + perfil.foto;
                document.getElementById('nombres').value = perfil.nombres;
                document.getElementById('apellidos').value = perfil.apellidos;
                document.getElementById('correo').value = perfil.correo;
                document.getElementById('fechaNac').innerHTML = perfil.fechaNac;
                document.getElementById('perf-carrera').innerHTML = perfil.carrera;
                document.getElementById('perf-prepa').innerHTML = perfil.prepa;
                if (perfil.trabaja === "Si") {
                    document.getElementById('perf-trabaja').innerHTML = `${perfil.trabaja}<br />
                            <strong class="me-auto">Empresa o lugar de trabajo:</strong>
                            <small>${perfil.empresa}</small>`;
                } else {
                    document.getElementById('perf-trabaja').innerHTML = perfil.trabaja;
                }
                document.getElementById('perf-lugarNac').innerHTML = perfil.lugarNac;
                document.getElementById('perf-sitSentimental').innerHTML = perfil.sitSentimental;
                document.getElementById('perf-gustos').innerHTML = perfil.gustos;

                if (perfil.correo == correo) {
                    footerModal.innerHTML = `<a class="btn btn-outline-primary" href="/perfil/editar">Editar</a>
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>`;
                } else {
                    footerModal.innerHTML = `<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>`;
                }

            });
}