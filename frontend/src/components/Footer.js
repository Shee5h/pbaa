import React from "react";
import "./Footer.css"
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {faFacebook, faTwitter, faInstagram} from "@fortawesome/free-brands-svg-icons";
import { NavLink } from "react-router-dom";


export default function Footer(){

    return (
        <div className="container-fluid bg-white footer">
            <div className="container">
                <div className="row pb-0">
                    <div className="col-8">
                        <div style={{marginLeft:"15px"}}>Copyright © 2022 taupyk. All rights reserved.</div>
                    </div>
                    <div className="col-1">
                        <NavLink to={"/contacts"} className="contact_footer">Susisiekite</NavLink>
                    </div>
                    <div className="col-3 brand__align">
                        <ul id="brands">
                            <li><a href="#" className="footer__brand__link"><FontAwesomeIcon icon={faInstagram} className="brand__icon"/></a></li>
                            <li><a href="#" className="footer__brand__link"><FontAwesomeIcon icon={faFacebook} className="brand__icon"/></a></li>
                            <li><a href="#" className="footer__brand__link"><FontAwesomeIcon icon={faTwitter} className="brand__icon"/></a></li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    )
}