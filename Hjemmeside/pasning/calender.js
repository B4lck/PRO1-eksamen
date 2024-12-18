// Objekt hvor kalenderen skal lægge elementer ind
const kalenderTarget = document.getElementById("calender");
// Titlen for kalenderen
let titleTarget = document.getElementById("month-title");


const today = new Date();

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
let targetMonth = today.getMonth()
let targetYear = today.getFullYear();
/**
 * Laver elementer i den meget flotte kalender
 */
function updateKalender() {
    kalenderTarget.innerHTML = `
        <h2 id="month-title"></h2>
        <div class="row">
            <div class="col-title">man</div>
            <div class="col-title">tir</div>
            <div class="col-title">ons</div>
            <div class="col-title">tor</div>
            <div class="col-title">fre</div>
            <div class="col-title">lør</div>
            <div class="col-title">søn</div>
        </div>`
    titleTarget = document.getElementById("month-title");

    const firstOfMonth = new Date(targetYear, targetMonth, 1);

    // Uge dag på den første dag i måneden (skal lige skubbes op da .getDay() starter på søndag
    let ugeDag = (firstOfMonth.getDay() + 6) % 7;

    const daysInMonth = getDaysInMonth(targetYear, targetMonth);

    let row;
    for (let i = -ugeDag, j = 0; i < daysInMonth + 7 -((daysInMonth + ugeDag) % 7); i++, j++) {
        if (j % 7 === 0) {
            row = document.createElement("div");
            row.className = "row";
            kalenderTarget.appendChild(row);
        }

        const date = new Date(firstOfMonth.getFullYear(), firstOfMonth.getMonth(), 1 + i);
        row.appendChild(createDayCol(date));
    }

    titleTarget.innerText = months[targetMonth] + " " + targetYear;
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
    if (date.getMonth() !== targetMonth) {
        col.style.color = "darkgray";
    }
    switch (getStateOfDate(date)) {
        case 3:
            col.classList.add("full");
            break;
        case 2:
            col.classList.add("almost-full");
            break;
        default:
            col.classList.add("open");
    }
    col.innerText = date.getDate();
    return col;
}


/**
 * liste over alle reservationer
 * @type {Reservation[]}
 */
let reservations = [];

/**
 * Klasse der indeholder en start- og slutdato
 */
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

/**
 * Højest antal reservationer på en dag
 * @type {number}
 */
const maxNumberOfCases = 30;
/**
 * Beskriver hvornår der er næsten fyldt for reservationer
 * @type {number}
 */
const almostFullCases = maxNumberOfCases * .7;

/**
 * Tjekker statussen på en dato, returnere 3 hvis fyldt, 2 hvis næsten fyldt og 1 hvis fri.
 * @param date Date objekt
 * @returns {number}
 */
function getStateOfDate(date) {
    let cases = 0;
    for (let reservation of reservations) {
        if (reservation.contains(date)) {
            cases++;
        }
    }
    if (cases >= maxNumberOfCases) {return 3}
    if (cases >= almostFullCases) {return 2}
    return 1;
}

// Henter alt data om reservationer
fetch("/data/reservations_public.csv")
.then(res => res.text())
.then(data => {
    let rows = data.split("\n");
    for (let i = 1; i < rows.length; i++) {
        let cols = rows[i].split(";");
        if (cols.length === 2) {

            reservations.push(new Reservation(parseDate(cols[0]), parseDate(cols[1])));
        }
    }
    updateKalender();
})

/**
 * Laver en dato ud fra en streng med formatet dd/mm/yyyy
 * @param dateString Streng med dd/mm/yyyy
 * @returns {Date}
 */
function parseDate(dateString) {
    let [d, m, y] = dateString.split("/").map(str => parseInt(str));
    return new Date(y, m - 1, d); // Minus 1, fordi javascript starter januar som 0.
}

/**
 * Går til næste måned
 */
function nextMonth() {
    targetMonth++;
    if (targetMonth > 11) {
        targetMonth = targetMonth % 12;
        targetYear++;
    }
    updateKalender();
}

/**
 * Går til forrige måned
 */
function prevMonth() {
    targetMonth--;
    if (targetMonth < 0) {
        targetMonth = 11;
        targetYear--;
    }
    updateKalender();
}