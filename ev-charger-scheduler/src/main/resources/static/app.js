async function loadSlots() {
    const date = document.getElementById("slotDate").value;
    const table = document.getElementById("slotsTable");

    table.innerHTML = `<tr><td colspan="7">Loading...</td></tr>`;

    try {
        const response = await fetch(`/slots?date=${date}`);

        if (!response.ok) {
            throw new Error(`Failed to load slots. HTTP ${response.status}`);
        }

        const slots = await response.json();

        if (slots.length === 0) {
            table.innerHTML = `<tr><td colspan="7">No open slots found.</td></tr>`;
            return;
        }

        table.innerHTML = "";

        slots.forEach(slot => {
            const row = document.createElement("tr");

            row.innerHTML = `
                <td>${slot.slotId}</td>
                <td>${slot.chargerLabel || "N/A"}</td>
                <td>${slot.location || "N/A"}</td>
                <td>${formatDateTime(slot.startTime)}</td>
                <td>${formatDateTime(slot.endTime)}</td>
                <td>${slot.status}</td>
                <td><button onclick="selectSlot(${slot.slotId})">Select</button></td>
            `;

            table.appendChild(row);
        });
    } catch (error) {
        table.innerHTML = `<tr><td colspan="7">${error.message}</td></tr>`;
    }
}

function selectSlot(slotId) {
    document.getElementById("slotId").value = slotId;
    document.getElementById("bookingResult").textContent =
        `Selected slot ${slotId}. Click Book to confirm.`;
}

async function bookAppointment() {
    const userId = Number(document.getElementById("userId").value);
    const slotId = Number(document.getElementById("slotId").value);
    const resultBox = document.getElementById("bookingResult");

    if (!userId || !slotId) {
        resultBox.textContent = "Please enter a valid userId and slotId.";
        return;
    }

    resultBox.textContent = "Booking appointment...";

    try {
        const response = await fetch("/appointments", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ userId, slotId })
        });

        const text = await response.text();

        let data;
        try {
            data = JSON.parse(text);
        } catch {
            data = text;
        }

        if (!response.ok) {
            resultBox.textContent = `Booking failed. HTTP ${response.status}\n\n${formatOutput(data)}`;
            await loadHealth();
            return;
        }

        resultBox.textContent = `Booking successful.\n\n${formatOutput(data)}`;

        await loadSlots();
        await loadAppointments();
        await loadHealth();
    } catch (error) {
        resultBox.textContent = `Request failed: ${error.message}`;
    }
}

async function loadAppointments() {
    const userId = Number(document.getElementById("appointmentsUserId").value);
    const table = document.getElementById("appointmentsTable");

    table.innerHTML = `<tr><td colspan="5">Loading...</td></tr>`;

    try {
        const response = await fetch(`/appointments?userId=${userId}`);

        if (!response.ok) {
            throw new Error(`Failed to load appointments. HTTP ${response.status}`);
        }

        const appointments = await response.json();

        if (appointments.length === 0) {
            table.innerHTML = `<tr><td colspan="5">No appointments found.</td></tr>`;
            return;
        }

        table.innerHTML = "";

        appointments.forEach(appt => {
            const row = document.createElement("tr");

            row.innerHTML = `
                <td>${appt.appointmentId}</td>
                <td>${appt.userId}</td>
                <td>${appt.slotId}</td>
                <td>${appt.status}</td>
                <td>${formatDateTime(appt.createdAt)}</td>
            `;

            table.appendChild(row);
        });
    } catch (error) {
        table.innerHTML = `<tr><td colspan="5">${error.message}</td></tr>`;
    }
}

async function loadHealth() {
    const resultBox = document.getElementById("healthResult");

    resultBox.textContent = "Loading health status...";

    try {
        const response = await fetch("/health");

        if (!response.ok) {
            throw new Error(`Health check failed. HTTP ${response.status}`);
        }

        const data = await response.json();
        resultBox.textContent = formatOutput(data);
    } catch (error) {
        resultBox.textContent = error.message;
    }
}

function formatOutput(data) {
    if (typeof data === "string") {
        return data;
    }
    return JSON.stringify(data, null, 2);
}

function formatDateTime(value) {
    if (!value) {
        return "";
    }

    // Handles normal ISO strings returned by Spring Boot.
    const date = new Date(value);
    if (!isNaN(date.getTime())) {
        return date.toLocaleString();
    }

    // Fallback if value is not directly parseable.
    return value;
}

window.addEventListener("load", () => {
    loadSlots();
    loadAppointments();
    loadHealth();
});