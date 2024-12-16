const kalenderTarget = document.getElementById("calender");
const titleTarget = document.getElementById("month-title");

/**
 * @param year {number}
 * @param month {number}
 * @returns {number}
 */
function getDaysInMonth(year, month) {
    return new Date(year, month + 1, 0).getDate();
}

/**
 * Navne på måneder
 * @type {string[]}
 */
let months = ["Januar", "Februar", "Marts", "April", "Maj", "Juni", "Juli", "August", "September", "Oktober", "November", "December"];

/**
 * Laver elementer i den meget flotte kalender
 */
function updateKalender() {
    const today = new Date();
    const firstOfMonth = new Date(today.getFullYear(), today.getMonth(), 1);

    // Uge dag på den første dag i måneden (skal lige skubbes op da .getDay() starter på søndag
    let ugeDag = (firstOfMonth.getDay() + 6) % 7;

    const daysInMonth = getDaysInMonth(today.getFullYear(), today.getMonth());

    let row;
    console.log(ugeDag);
    for (let i = -ugeDag, j = 0; i < daysInMonth + (daysInMonth + ugeDag) % 7; i++, j++) {
        if (j % 7 === 0) {
            row = document.createElement("div");
            row.className = "row";
            kalenderTarget.appendChild(row);
        }

        const date = new Date(firstOfMonth.getFullYear(), firstOfMonth.getMonth(), 1 + i);
        row.appendChild(createDayCol(date));
    }

    titleTarget.innerText = months[firstOfMonth.getMonth()];
}

/**
 * @param date {Date}
 * @returns {HTMLDivElement}
 */
function createDayCol(date) {
    let today = new Date();
    const col = document.createElement("div");
    col.className = "col";
    if (date.getFullYear() === today.getFullYear() &&
        date.getMonth() === today.getMonth() &&
        date.getDate() === today.getDate()) {
        col.classList.add("today");
    }
    if (date.getMonth() !== today.getMonth()) {
        col.style.color = "darkgray";
    }
    if (isFull(date)) {
        col.classList.add("full");
    }
    col.innerText = date.getDate();
    return col;
}


/**
 * liste over alle reservationer
 * @type {Reservation[]}
 */
let reservations = [];

class Reservation {
    startDate;
    endDate
    constructor(startDate, endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    contains(date) {
        return  this.startDate.getFullYear() <= date.getFullYear() &&
                this.startDate.getMonth() <= date.getMonth() &&
                this.startDate.getDate() <= date.getDate() &&
                this.endDate.getFullYear() >= date.getFullYear() &&
                this.endDate.getMonth() >= date.getMonth() &&
                this.endDate.getDate() >= date.getDate();
    }
}

function isFull(date) {
    let cases = 0;
    for (let reservation of reservations) {
        if (reservation.contains(date)) {
            cases++;
        }
    }
    return cases >= 30;
}

fetch("/data/reservations_public.csv")
.then(res => res.text())
.then(data => {
    let rows = data.split("\n");
    for (let i = 1; i < rows.length; i++) {
        let cols = rows[i].split(";");
        if (cols.length === 2) {

            reservations.push(new Reservation(new Date(cols[0]), new Date(cols[1])));
        }
    }
    updateKalender();
})