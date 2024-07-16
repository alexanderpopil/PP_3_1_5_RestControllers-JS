document.addEventListener('DOMContentLoaded', function () {
    function fetchUserInfo() {
        fetch('/api/users/me')
            .then(response => response.json())
            .then(user => {
                document.getElementById('navbar-username').textContent = user.username;
                document.getElementById('navbar-roles').textContent = user.roles.map(role => role.name.substring(5)).join(', ');

                const userInfoTable = document.getElementById('user-info');
                userInfoTable.innerHTML = `
                    <tr>
                        <td>${user.id}</td>
                        <td>${user.username}</td>
                        <td>${user.email}</td>
                        <td>${user.age}</td>
                        <td>${user.roles.map(role => role.name.substring(5)).join(', ')}</td>
                    </tr>
                `;
            });
    }

    fetchUserInfo();
});