document.addEventListener("DOMContentLoaded", function () {
    const searchInput = document.getElementById("nombre");
    const searchResultsContainer = document.getElementById("search-results");
    const listUsersDiv = document.getElementById("list-users");

    searchInput.addEventListener("input", function () {
        const searchTerm = searchInput.value;
        console.log("searchTerm:", searchTerm);

        if (searchTerm.trim() !== "") {
            const url = `http://localhost:8080/solicitudes/buscar?nombre=${searchTerm}`;
            console.log("url:", url);

            fetch(url)
                .then(response => {
                    console.log("response:", response);
                    return response.text();
                })
                .then(data => {
                    console.log("data:", data);
                    listUsersDiv.style.display = "none";
                    searchResultsContainer.innerHTML = data;
                })
                .catch(error => {
                    location.reload();
                    console.error("Error al realizar la búsqueda:", error);
                });
        } else {
            searchResultsContainer.innerHTML = "";
            listUsersDiv.style.display = "block";
        }
    });
});
