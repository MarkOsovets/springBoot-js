// ===================
// Общие переменные
// ===================
const API_URL = "http://localhost:8080/reservations";

const addBtn = document.getElementById("add");
const loadBtn = document.getElementById("load");
const list = document.getElementById("list");

// input'ы (предполагаем, что id есть в HTML)
const userIdInput = document.getElementById("userId");
const roomIdInput = document.getElementById("roomId");
const startDateInput = document.getElementById("startDate");
const endDateInput = document.getElementById("endDate");

const filterUserId = document.getElementById("filterUserId");
const filterRoomId = document.getElementById("filterRoomId");


// ===================
// Navbar
// ===================
const addSection = document.getElementById("add-section");
const listSection = document.getElementById("list-section");
const homeSection = document.getElementById("home-section");

const navAdd = document.getElementById("nav-add");
const navList = document.getElementById("nav-list");
const navHome = document.getElementById("nav-home");

navHome.addEventListener("click", (e) => {
    e.preventDefault(); // ⬅️ важно
    addSection.style.display = "none";
    listSection.style.display = "none";
    homeSection.style.display = "block";
});

navAdd.addEventListener("click", (e) => {
    e.preventDefault(); // ⬅️ важно
    addSection.style.display = "block";
    listSection.style.display = "none";
    homeSection.style.display = "none";
});

navList.addEventListener("click", (e) => {
    e.preventDefault(); // ⬅️ важно
    addSection.style.display = "none";
    listSection.style.display = "block";
    homeSection.style.display = "none";
});




// ===================
// POST — добавление
// ===================
addBtn.addEventListener("click", async () => {
    try {
        const response = await fetch(API_URL, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                userId: Number(userIdInput.value),
                roomId: Number(roomIdInput.value),
                startDate: startDateInput.value,
                enDate: endDateInput.value
            })
        });

        if (!response.ok) {
            throw new Error("Ошибка при создании брони");
        }

        const data = await response.json();

        const userChoice = confirm(
            "Бронь добавлена!\n\n" +
            "Нажмите OK чтобы увидеть детали,\n" +
            "Или Отмена чтобы скрыть."
        );

        if (userChoice) {
            alert(JSON.stringify(data, null, 2));
        }
    } catch (e) {
        console.error(e);
        alert("Не удалось добавить бронь");
    }
});

// ===================
// GET — список
// ===================
loadBtn.addEventListener("click", async () => {
    try {
        let url = `${API_URL}?pageSize=5&pageNumber=0`;

        if (filterUserId.value) {
            url += `&userId=${filterUserId.value}`;
        }
        if (filterRoomId.value) {
            url += `&roomId=${filterRoomId.value}`;
        }

        const response = await fetch(url);

        if (!response.ok) {
            throw new Error("Ошибка загрузки списка");
        }

        const data = await response.json();
        console.log("Ответ сервера:", data);

        render(data.content ?? data);
    } catch (e) {
        console.error(e);
        alert("Не удалось загрузить список");
    }
});

// ===================
// Render
// ===================
function render(reservations) {
    list.innerHTML = "";

    reservations.forEach(r => {
        if (r.status !== "CANCELLED"){ 
        const li = document.createElement("li");
        li.textContent = `id: ${r.id} | user: ${r.userId} | room: ${r.roomId}`;

        const del = document.createElement("button");
        del.textContent = "Удалить";
        del.onclick = () => deleteReservation(r.id);

        const approve = document.createElement("button");
        approve.textContent = "Подтвердить";
        approve.onclick = () => approveReservation(r.id);

        li.append(" ", del, " ", approve);
        list.appendChild(li);
        }
    }   );
}

// ===================
// DELETE
// ===================
async function deleteReservation(id) {
    try {
        const response = await fetch(`${API_URL}/${id}/cancel`, {
            method: "DELETE"
        });

        if (!response.ok) {
            throw new Error("Ошибка удаления");
        }

        alert("Удалено");
    } catch (e) {
        console.error(e);
        alert("Не удалось удалить бронь");
    }
}

// ===================
// POST — подтверждение
// ===================
async function approveReservation(id) {
    try {
        const response = await fetch(`${API_URL}/approved/${id}`, {
            method: "POST"
        });

        if (!response.ok) {
            throw new Error("Ошибка подтверждения");
        }

        alert("Подтверждено");
    } catch (e) {
        console.error(e);
        alert("Не удалось подтвердить бронь");
    }
}
