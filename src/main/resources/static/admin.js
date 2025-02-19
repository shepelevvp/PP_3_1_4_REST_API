document.addEventListener('DOMContentLoaded', authentication);
document.addEventListener('DOMContentLoaded', header);
document.addEventListener('DOMContentLoaded', showAdminPanel);
document.addEventListener('DOMContentLoaded', userProfile);
document.addEventListener('DOMContentLoaded', allRolesForm);

document.getElementById('addUserForm').addEventListener('submit', async (event) => {
    console.log('Add user form submitted');
    event.preventDefault();
    await addNewUser();
});

async function authentication() {
    const response = await fetch('/api/authUser')
    if (!response.ok) {
        throw new Error('Сетевой ответ не в формате OK');
    }
    const authUser = await response.json();
    const rolesAuthUser = authUser.roles.map(role => getReadableRole(role.role)).join(', ');

    const adminContent = document.getElementById("admin-panel");
    const userProfile = document.getElementById("user-profile");
    const navigation = document.getElementById("navigation");
    const buttonAdmin = document.getElementById('button-admin-panel');
    const buttonUser = document.getElementById('button-user-profile');

    if (rolesAuthUser.includes("ADMIN")) {
        adminContent.classList.add('show', 'active');
        buttonAdmin.classList.add('active');
        buttonAdmin.setAttribute('aria-selected', 'true');
        buttonUser.setAttribute('aria-selected', 'false');
        navigation.appendChild(buttonAdmin);
    } else {
        adminContent.style.display = "none";
        userProfile.classList.add('show', 'active');
        buttonAdmin.style.display = "none";
        buttonUser.classList.add('nav-link', 'active');
        buttonUser.setAttribute('aria-selected', 'true');
    }
    navigation.appendChild(buttonUser);
}


async function userProfile() {
    fetch('/api/authUser')
        .then(response => {
            if (!response.ok) {
                throw new Error('Сетевой ответ не в формате OK');
            }
            return response.json();
        })
        .then(user => {
            const userTableBody = document.getElementById('userTableBody');

            // Проверяем, существует ли элемент таблицы
            if (!userTableBody) {
                console.error("Элемент таблицы не найден");
                return;
            }

            // Создаем новую строку для таблицы
            const row = document.createElement('tr');
            row.classList.add('table-light');

            // Добавляем ячейки с данными пользователя
            row.innerHTML = `
                <td>${user.id}</td>
                <td>${user.name}</td>
                <td>${user.lastname}</td>
                <td>${user.age}</td>
                <td>${user.email}</td>
                <td>${user.roles.map(role => getReadableRole(role.role)).join(', ')}</td>
            `;

            // Добавляем строку в таблицу
            userTableBody.appendChild(row);
        })
        .catch(error => {
            console.error("Ошибка при загрузке данных пользователя:", error);
        });
}

async function header() {
    const response = await fetch('/api/authUser')
    if (!response.ok) {
        throw new Error('Сетевой ответ не в формате OK');
    }
    const authUser = await response.json();

    const headerUserAuthName = document.getElementById('headerUserAuthName');

    if (headerUserAuthName) {
        headerUserAuthName.textContent = authUser.email;
    } else {
        console.error('Элемент с id ' + headerUserAuthName + ' не найден');
    }

    const headerRolesUserAuth = document.getElementById("headerRolesUserAuth");

    if (headerRolesUserAuth) {
        headerRolesUserAuth.textContent = authUser.roles.map(role => getReadableRole(role.role)).join(', ')
    }
}


async function allRolesForm() {
    const roles = await showAllRoles();
    const allRolesContainer = document.getElementById("newUserRoles");
    allRolesContainer.innerHTML = '';

    roles.forEach(r => {
        const option = document.createElement('option');
        option.value = r.id;
        option.textContent = r.role;
        allRolesContainer.appendChild(option);
    })
}

function getReadableRole(role) {
    return role.replace("ROLE_", ""); // Убираем префикс ROLE_
}

function hasRole(userRoles, role) {
    return userRoles.some(r => r.role === role);
}

async function showAllRoles() {
    const response = await fetch('/api/adminpanel/roles');
    if (!response.ok) {
        throw new Error('Ошибка при загрузке ролей');
    }
    return await response.json();
}

async function showAdminPanel() {

    try {
        const response = await fetch('/api/adminpanel/users')
        if (!response.ok) {
            throw new Error('Сетевой ответ не в формате OK');
        }
        const users = await response.json();
        const usersTableBody = document.getElementById('usersTableBody');

        // Проверяем, существует ли элемент таблицы
        if (!usersTableBody) {
            console.error("Элемент таблицы не найден");
            return;
        }

        // Очищаем таблицу перед добавлением новых данных (если нужно)
        usersTableBody.innerHTML = '';

        // Для каждого пользователя создаем строку в таблице
        users.forEach(user => {
            const row = document.createElement('tr');
            // const readableRoles = user.roles.map(role => getReadableRole(role.role)).join(', ');

            // Добавляем ячейки с данными пользователя
            row.innerHTML = `
            <td>${user.id}</td>
            <td>${user.name}</td>
            <td>${user.lastname}</td>
            <td>${user.age}</td>
            <td>${user.email}</td>
            <td>${user.roles.map(role => getReadableRole(role.role)).join(', ')}</td>
            <td>
                <button class="btn btn-info btn-sm text-white"
                    data-bs-toggle="modal" onclick="showEditModal(${user.id})">Edit
                </button>
            </td>
            <td>
                <button class="btn btn-danger btn-sm text-white" 
                    data-bs-toggle="modal" onclick="showDeleteModal(${user.id})">Delete
                </button>
            </td>
        `;

            // Добавляем строку в таблицу
            usersTableBody.appendChild(row);
        })
    } catch(error) {
        console.error("Ошибка при загрузке данных пользователя:", error);
    }
}

async function showDeleteModal(userId) {
    try {
        const response = await fetch(`/api/adminpanel/users/${userId}`);
            if (!response.ok) {
                throw new Error('Сетевой ответ не в формате OK');
            }

        const user = await response.json();
        document.getElementById('deleteId').value = user.id;
        document.getElementById('deleteName').value = user.name;
        document.getElementById('deleteLastname').value = user.lastname;
        document.getElementById('deleteAge').value = user.age;
        document.getElementById('deleteEmail').value = user.email;
        document.getElementById('deleteRoles').value = user.roles
            .map(role => getReadableRole(role.role)).join(', ');

        const deleteUserModal = new bootstrap.Modal(document.getElementById('deleteUserModal'));
        deleteUserModal.show();
        document.getElementById('deleteButton').onclick = async (event) => {
        //     document.getElementById('deleteButton').addEventListener( 'click', async (event) => {
            event.preventDefault();
            await deleteUser(userId);
            deleteUserModal.hide();
            await showAdminPanel();
        }
    } catch (error) {
        console.error('Ошибка при выборе пользователя для удаления:', error);
    }
}

async function deleteUser(userId){
    const response = await fetch(`/api/adminpanel/users/${userId}`, {
        method: 'DELETE'
    });
    if (!response.ok) {
        throw new Error("Ошибка при удалении пользователя")
    }
    return true;
}

async function showEditModal(userId) {
    try {
        const response = await fetch(`/api/adminpanel/users/${userId}`);
        if (!response.ok) {
            throw new Error('Сетевой ответ не в формате OK');
        }

        const user = await response.json();
        document.getElementById('editId').value = user.id;
        document.getElementById('editName').value = user.name;
        document.getElementById('editLastname').value = user.lastname;
        document.getElementById('editAge').value = user.age;
        document.getElementById('editEmail').value = user.email;

        const roles = await showAllRoles();
        const rolesContainer = document.getElementById('editRoles');
        rolesContainer.innerHTML = ''; // Очищаем контейнер

        roles.forEach(role => {
            const roleElement = document.createElement('option');
            roleElement.value = role.role;
            roleElement.textContent = getReadableRole(role.role);
            if (hasRole(user.roles, role.role)) {
                roleElement.selected = true; // Выделяем выбранную роль
            }
            rolesContainer.appendChild(roleElement);
        });

        const editUserModal = new bootstrap.Modal(document.getElementById('editUserModal'));
        editUserModal.show();

        document.getElementById('editButton').onclick = async (event) => {
            event.preventDefault();

            const selectedRoles = Array.from(rolesContainer.selectedOptions)
                .map(option => ({ role: option.value }));

            const updatedUser = {
                id: document.getElementById('editId').value,
                name: document.getElementById('editName').value,
                lastname: document.getElementById('editLastname').value,
                age: document.getElementById('editAge').value,
                email: document.getElementById('editEmail').value,
                password: document.getElementById('editPassword').value,
                roles: selectedRoles || []
            };
            await editUser(updatedUser);
            editUserModal.hide();
            await showAdminPanel();
        };
    } catch (error) {
        console.error('Ошибка при получении данных пользователя:', error);
        alert('Ошибка при получении данных пользователя');
    }
}

async function editUser(updatedUser) {
    console.log(updatedUser)
    try {
        const response = await fetch(`/api/adminpanel/users`, {
            method: 'PUT', // Используем PUT для обновления данных
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(updatedUser) // Отправляем UserDTO в теле запроса
        });
        if (!response.ok) {
            const errorData = await response.json();// Ожидаем JSON с ошибками
            console.log(errorData);
            const prefix = "edit-";
            displayErrors(errorData, prefix);
            editUser.preventDefault();
        } else {
            alert('Данные пользователя успешно изменены!');
            document.getElementById('editUserForm').reset();
            const errorElements = document.querySelectorAll('.error-message');
            errorElements.forEach(el => el.remove());
        }
        return true;
    } catch (error) {
        console.error('Ошибка при редактировании пользователя:', error);
        throw error; // Пробрасываем ошибку для обработки в вызывающем коде
    }
}

async function addNewUser() {
    try {
        const newUser = {
            name: document.getElementById('newName').value,
            lastname: document.getElementById('newLastname').value,
            age: document.getElementById('newAge').value,
            email: document.getElementById('newEmail').value,
            password: document.getElementById('newPassword').value,
            roles: Array.from(document.getElementById('newUserRoles').selectedOptions).map(option => option.textContent)
        };

        await saveNewUser(newUser);
        await showAdminPanel();
    } catch (error) {
        console.error('Ошибка при получении данных пользователя:', error);
        alert('Ошибка при получении данных пользователя');
    }
}

async function saveNewUser(newUser) {
    try {
        const errorElements = document.querySelectorAll('.error-message');
        errorElements.forEach(el => el.remove());

        const response = await fetch(`/api/adminpanel/users`, {
            method: 'POST', // Используем POST для добавления нового пользователя
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(newUser) // Отправляем UserDTO в теле запроса
        });
        if (!response.ok) {
            const errorData = await response.json();// Ожидаем JSON с ошибками
            console.log(errorData);
            const prefix = "new-";
            displayErrors(errorData, prefix); // Отображаем ошибки
        } else {
            alert('Пользователь добавлен!');
            document.getElementById('addUserForm').reset();
        }
        return true;
    } catch (error) {
        console.error('Ошибка при добавлении пользователя:', error);
        throw error; // Пробрасываем ошибку для обработки в вызывающем коде
    }
}

function displayErrors(errors, prefix) {
    console.log(errors);
    // Очистка предыдущих ошибок
    const errorElements = document.querySelectorAll('.error-message');
    errorElements.forEach(el => el.remove());

    // Отображение новых ошибок
    for (const [field, message] of Object.entries(errors)) {
        console.log(Object.entries(errors));
        const input = document.querySelector(`[name="${prefix}${field}"]`);
        console.log(field);
        console.log(message);
        if (input) {
            const errorElement = document.createElement('div');
            console.log(errorElement);
            errorElement.className = 'error-message text-danger';
            errorElement.textContent = message;
            console.log(message);
            input.parentNode.appendChild(errorElement);
            console.log(input.parentNode);
        }
    }
}