import React from "react";
import "./Header.css"

export default function Header() {

    return <div className="container-fluid">
        <div className="row header-config">
            <div className="col-lg-7 col-sm-5">
                <img src="https://via.placeholder.com/150x70?text=LOGO" alt="placeholder" className="img-config"></img>
            </div>
            <div className="col-lg-2 col-sm-1">
                <img src="https://via.placeholder.com/90x70?text=Accounts" alt="placeholder" className="img-config"></img>
            </div>
            <div className="col-lg-3 col-sm-6 pt-4">
                <p>User information (Work in progress)</p>
            </div>
        </div>
    </div>
}