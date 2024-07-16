document.addEventListener('DOMContentLoaded', function () {
    const userTableBody = document.getElementById('user-table-body');

    function fetchUsers() {
        fetch('/api/users')
            .then(response => response.json())
            .then(users => {
                userTableBody.innerHTML = '';
                users.forEach(user => {
                    userTableBody.innerHTML += `
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.username}</td>
                            <td>${user.email}</td>
                            <td>${user.age}</td>
                            <td>${user.roles.map(role => role.name.substring(5)).join(', ')}</td>
                            <td><button class="btn btn-info" data-toggle="modal" data-target="#editModal" data-id="${user.id}">Edit</button></td>
                            <td><button class="btn btn-danger" data-toggle="modal" data-target="#deleteModal" data-id="${user.id}">Delete</button></td>
                        </tr>
                    `;
                });
                attachEditEvent();
                attachDeleteEvent();
            });
    }

    function fetchRoles(selectElement) {
        fetch('/api/roles')
            .then(response => response.json())
            .then(roles => {
                selectElement.innerHTML = '';
                roles.forEach(role => {
                    selectElement.innerHTML += `<option value="${role.id}">${role.name}</option>`;
                });
            });
    }

    function attachEditEvent() {
        document.querySelectorAll('[data-target="#editModal"]').forEach(button => {
            button.addEventListener('click', event => {
                const userId = event.target.getAttribute('data-id');
                fetch(`/api/users/${userId}`)
                    .then(response => response.json())
                    .then(user => {
                        document.getElementById('edit-user-id').value = user.id;
                        document.getElementById('edit-username').value = user.username;
                        document.getElementById('edit-email').value = user.email;
                        document.getElementById('edit-age').value = user.age;
                        document.getElementById('edit-password').value = user.password;
                        const rolesSelect = document.getElementById('edit-roles');
                        fetchRoles(rolesSelect);
                        setTimeout(() => {
                            user.roles.forEach(role => {
                                rolesSelect.querySelector(`option[value="${role.id}"]`).selected = true;
                            });
                        }, 500);
                    });
            });
        });
    }

    function attachDeleteEvent() {
        document.querySelectorAll('[data-target="#deleteModal"]').forEach(button => {
            button.addEventListener('click', event => {
                const userId = event.target.getAttribute('data-id');
                fetch(`/api/users/${userId}`)
                    .then(response => response.json())
                    .then(user => {
                        document.getElementById('delete-user-id').value = user.id;
                        document.getElementById('delete-username').value = user.username;
                        document.getElementById('delete-email').value = user.email;
                        document.getElementById('delete-age').value = user.age;
                        const rolesSelect = document.getElementById('delete-roles');
                        fetchRoles(rolesSelect);
                        setTimeout(() => {
                            user.roles.forEach(role => {
                                rolesSelect.querySelector(`option[value="${role.id}"]`).selected = true;
                            });
                        }, 500);
                        document.getElementById('delete-username-confirm').textContent = user.username;
                    });
            });
        });
    }

    document.getElementById('edit-user-form').addEventListener('submit', function (event) {
        event.preventDefault();
        const userId = document.getElementById('edit-user-id').value;
        const formData = new FormData(event.target);
        const data = Object.fromEntries(formData.entries());
        data.roles = Array.from(document.getElementById('edit-roles').selectedOptions).map(option => ({ id: option.value }));
        fetch(`/api/users/${userId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        }).then(response => {
            if (response.ok) {
                fetchUsers();
                $('#editModal').modal('hide'); //Проверить закрытие модалки
            }
        });
    });

    document.getElementById('delete-user-form').addEventListener('submit', function (event) {
        event.preventDefault();
        const userId = document.getElementById('delete-user-id').value;
        fetch(`/api/users/${userId}`, {
            method: 'DELETE'
        }).then(response => {
            if (response.ok) {
                fetchUsers();
                $('#deleteModal').modal('hide'); //Проверить закрытие модалки
            }
        });
    });

    fetchUsers();
});