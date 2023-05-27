import React from "react";

export default function SideBar() {

    return <div style={{ borderLeft: "solid 1px #696969" }}>
        <div className="row mt-3">
            <div className="col-2">
                <img src="https://via.placeholder.com/60x60?text=$" alt="placeholder" className="img-config"></img>
            </div>
            <div className="col-3">
                <h4>Likutis</h4>
                <h3>$YYYYYYY,00</h3>
            </div>
        </div>
        {/* row end */}
        <hr></hr>
        <div className="container">
            <h4>Recent activities</h4>
            <h4>.</h4>
            <h4>.</h4>
            <h4>.</h4>
            <h6>View more...</h6>
        </div>
    </div>
}