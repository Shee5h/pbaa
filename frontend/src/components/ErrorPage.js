import React from "react";
import './ErrorPage.css'

export default function ErrorPage(){

    return(
        <div className="bg">
            <div className="container centerError">
                <img className="error" src={require('../images/404ErrorPage.gif')}></img>
                {/* <div className="hero" style={{ backgroundImage: `url(${require("../images/404ErrorPage.gif")})` }} /> */}
            </div>
        </div>
            
    )
}