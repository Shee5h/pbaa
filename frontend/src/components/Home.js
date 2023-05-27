import React from "react";
import "./Home.css"

const Home = () => {
    return (
        <>

            <div className="container mt-custom box-shadow bg-white">
                {/* why use a budget app row */}
                <div className="row">
                    <div className="">
                        <div className="col-11 col-md-11 bg-white second_section container">
                            <div className="row">
                                <div className="row ss_header">
                                    <div className="col-4">
                                        <div>Atraskite savo blogus išlaidų įpročius</div>
                                    </div>
                                    <div className="col-4">
                                        <div>Lengviau, nei rinkti kvitus</div>
                                    </div>
                                    <div className="col-4">
                                        <div>Siekite savo finansinių tikslų</div>
                                    </div>
                                </div>
                                <div className="row ss_content">
                                    <div className="col-4">
                                        <div>Su "taupyk" aplikacijos įrankiais, sekdami savo pajamas ir išlaidas, galite identifikuoti savo blogus išlaidų įpročius. Taip kitą kartą sutaupysite, nepirkdami nereikalingų produktų.</div>
                                    </div>
                                    <div className="col-4">
                                        <div>Galite pamiršti apie kaupiamus kvitus savo piniginėje ar striukės kišenėje, nes visas savo išlaidas galite sekti savo paskyroje.</div>
                                    </div>
                                    <div className="col-4">
                                        <div>Turite kokį finansinį tikslą? Norite sutaupyti naujam automobiliui? O gal išvykti į užsienį atostogoms? Lengvai planuokites savo finansus ir sekite užsibriežtų tikslų su "taupyk".</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                {/* benefits for you row */}
                <div className="row">
                    <div className="container">
                        <div className="col-11 col-md-11 bg-white third_section container">
                            <div className="row">
                                <div className="col-4 ss_header">Planuokite lengviau</div>
                                <div className="col-8 ss_content">Geras pinigų valdymas – tai kruopštus planavimas. Lengva pasimesti savo išlaidose, jei tiksliai nematote kiek, kur ir kada išleidote savo turimas lėšas.</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </>
    );
};
export default Home;
