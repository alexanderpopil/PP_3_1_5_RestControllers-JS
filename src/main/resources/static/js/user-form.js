document.addEventListener('DOMContentLoaded', function () {
    function fetchRoles() {
        // fetch('/api/roles')
        fetch('/api/admin/roles')
            .then(response => response.json())
            .then(roles => {
                const rolesSelect = document.getElementById('roles');
                roles.forEach(role => {
                    rolesSelect.innerHTML += `<option value="${role.id}">${role.name.substring(5)}</option>`;
                });
            });
    }

    function fetchUserInfo() {
        // fetch('/api/users/me')
        fetch('/api/admin/users/me')
            .then(response => response.json())
            .then(user => {
                document.getElementById('navbar-username').textContent = user.username;
                document.getElementById('navbar-roles').textContent = user.roles.map(role => role.name.substring(5)).join(', ');
            });
    }

    fetchUserInfo();

    document.getElementById('user-form').addEventListener('submit', function (event) {
        event.preventDefault();
        const formData = new FormData(event.target);
        const data = Object.fromEntries(formData.entries());
        data.roles = Array.from(document.getElementById('roles').selectedOptions).map(option => ({ id: option.value }));
        // fetch('/api/users', {
        fetch('/api/admin/users', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        }).then(response => {
            if (response.ok) {
                window.location.href = '/admin';
            }
        });
    });

    fetchRoles();
});