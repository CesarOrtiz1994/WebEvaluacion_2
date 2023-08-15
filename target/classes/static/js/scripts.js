const toastTrigger = document.getElementById('solicitudesToast');
const toastElList = document.querySelectorAll('.toast');

if (toastTrigger) {
    for (var i = 0; i < toastElList.length; i++) {
        console.log(toastElList[i]);
        const toastBootstrap = bootstrap.Toast.getOrCreateInstance(toastElList[i]);
        toastTrigger.addEventListener('click', () => {
            toastBootstrap.show();
        });
    }

}