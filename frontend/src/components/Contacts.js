import React from 'react'



function Contacts() {
  return (
    <div>
        
        <div className="container " >
      <div className="row ">
          <div className="col-md-6">
             
              <h3>Apsilankykite vienoje iš mūsų biuro vietų arba susisiekite su mumis šiandien</h3>
              <p><b>Pagrindinis biuras</b></p>
              <ul className="fa-ul">
                  <li>
                      {/* <span className="fa-li"><i className="fas fa-map"></i></span> */}
                      Kalvarijų g. 159, Vilnius 08313</li>
                  <li>
                      {/* <span className="fa-li"><i className="fas fa-envelope"></i></span> */}
                      El. paštas vtmc@vtmc.lt</li>
                  <li>
                      {/* <span className="fa-li"><i className="fas fa-phone"></i></i></span> */}
                      Tel. / faks. (8 5) 247 7560</li>
                  <li>
                      {/* <span className="fa-li"><i className="fas fa-clock"></i></span> */}
                  Monday to Saturday 9:00 to 16:00</li>
                  
              </ul>
          </div>
          {/* <div className="col-md-6">
            <div id="map-container-google-3" className="z-depth-1-half map-container-3">
                <iframe src="https://maps.google.com/maps?q=Kalvarij%C5%B3%20g.%20159,%20Vilnius%2008313&t=&z=15&ie=UTF8&iwloc=&output=embed" frameborder="0"
                  style="border:0" allowfullscreen></iframe>
              </div>
          </div> */}
      </div>
    </div>
      
    </div>

  )
}

export default Contacts