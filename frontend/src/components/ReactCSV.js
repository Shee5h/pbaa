import React, { useState, useEffect } from "react";
import { CSVLink } from 'react-csv';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'


import AuthService from "../services/auth.service";

const ReactCSV = (props) => {

    const data = [
        ["Aprašymas", "Data", "Kategorija", "Kiekis"],
        // [expense.expenseName, expense.date, expense.expensesCategory.name, expense.amount]

    ];

    const expenses = props.allExpenses.map((ex) => ({
        Nr: ex.id,
        Aprašymas: ex.expenseName,
        Data: ex.date,
        Kategorija: ex.expensesCategory.name,
        Kiekis: ex.amount
    }));
    // const headers = [
    //   { label: 'Nr', key: 'id'},
    //   { label: 'Aprašymas', key: 'expenseName'},
    //   { label: 'Data', key: 'date'},
    //   { label: 'Kategorija', key: 'date'},
    //   { label: 'Data', key: 'date'}
    // ];


    return (
        <div>
            <CSVLink
                data={expenses}
                // headers={headers}
                filename={"Išlaidos"}
                target="_blank"
                style={{ color: "white" }}
            >
                Atsisiųsti išlaidas <FontAwesomeIcon icon="download" style={{ paddingBottom: 2.5 }} />
            </CSVLink>
        </div>
    )
};

export default ReactCSV;