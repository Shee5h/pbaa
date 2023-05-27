import React, { useState, useEffect, Component } from "react";
import "./IncomeAndExpense.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCirclePlus } from "@fortawesome/free-solid-svg-icons";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import AuthService from "../services/auth.service";
import { useForm } from "react-hook-form";
import EditIncomeModal from './EditIncomeModal';
import DeleteModal from './DeleteModal';
import ReactPaginate from 'react-paginate';
import Table from 'react-bootstrap/Table';
import Pagination from '../components/Pagination';
import uuid from "react-uuid";

// This code copypasted from: https://codepen.io/fido123/pen/xzvxNw
// JavaScript is not included in this code, only html and css

export default function Income() {
  const [allIncome, setAllIncome] = useState([]);
  const [forceRender, setForceRender] = useState(false);
  const [deleteId, setDeleteId] = useState();
  const currentUser = AuthService.getCurrentUser();
  const [displayDeleteModal, setDisplayDeleteModal] = useState(false);
  const {
    register,
    handleSubmit,
    formState: { errors },
    reset,
  } = useForm({ mode: "onSubmit", reValidateMode: "onSubmit" });
  // Sums user's income
  //const incomeSum = allIncome.reduce((n, { amount }) => n + amount, 0);

  // This is used to figure out today's date, and format it accordingly
  let today = new Date();
  const dd = String(today.getDate()).padStart(2, "0");
  const mm = String(today.getMonth() + 1).padStart(2, "0");
  const yyyy = today.getFullYear();
  today = yyyy + "-" + mm + "-" + dd;

  // Limiting not older than... date
  let dateLimit = new Date("2000-01-01");
  //

  // let dDate = new Date().toLocaleDateString('en-CA');

  // Add user's income to database from the inputs
  const onSubmit = async (data, e) => {
    const response = await fetch("http://localhost:8080/api/income/", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${currentUser.accessToken}`,
      },
      body: JSON.stringify({
        incomeName: data.incomeName,
        date: data.date,
        amount: data.amount,
      }),
    });

    if (response.status === 201) {
      successMessage("Pridėta!");
      reset();
    } else {
      errorMessage("Klaida!");
    }

    setForceRender(!forceRender);
  };

  // Popup message configuration
  toast.configure();
  const successMessage = (msg) => {
    toast.success(msg, {
      position: toast.POSITION.TOP_CENTER,
      autoClose: 3000,
      theme: "colored",
      pauseOnHover: false,
      hideProgressBar: true,
    });
  };
  const errorMessage = (msg) => {
    toast.error(msg, {
      position: toast.POSITION.TOP_CENTER,
      autoClose: 3000,
      theme: "colored",
      pauseOnHover: false,
      hideProgressBar: true,
    });
  };

  const removeIncome = async (id) => {
    const response = await fetch(`http://localhost:8080/api/income/${id}`, {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${currentUser.accessToken}`,
      },
    });

    if (response.status === 200) {
      successMessage("Ištrinta");
    } else {
      errorMessage("Klaida!");
    }

    setForceRender(!forceRender);
    setDisplayDeleteModal(false);
  };

  const showDeleteModal = (id) => {
    setDisplayDeleteModal(true);
    setDeleteId(id);
  };

  const hideDeleteModal = () => {
    setDisplayDeleteModal(false);
  };

  // Fetch all user's income from database to display down below
  // useEffect(() => {
  //     const fetchData = async () => {
  //         const response = await fetch(`http://localhost:8080/api/income/user/${currentUser.id}`,
  //             {
  //                 method: "GET",
  //                 headers: {
  //                     'Content-Type': 'application/json',
  //                     'Authorization': `Bearer ${currentUser.accessToken}`
  //                 }
  //             });
  //         const data = await response.json();
  //         setAllIncome(data);
  //     };

  //     fetchData();
  // }, [forceRender]);

  // to sum all user income and display it at the top

  const [allIncome2, setAllIncome2] = useState([]);
  const incomeSum = allIncome2.reduce((n, { amount }) => n + amount, 0);
  useEffect(() => {
    const fetchData = async () => {
      const response = await fetch(`http://localhost:8080/api/income/user/${currentUser.id}`,
        {
          method: "GET",
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${currentUser.accessToken}`
          }
        });
      const data = await response.json();
      setAllIncome2(data);
    };

    fetchData();
  }, [forceRender]);

  //pagination..........................

  const [pageCount, setpageCount] = useState(0);
  //limit of how many items per page to see
  let limit = 9;

  useEffect(() => {
    const fetchData = async () => {
      const response = await fetch(
        `http://localhost:8080/api/income/user?offset=0&pageSize=${limit}`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${currentUser.accessToken}`,
          },
        }
      );
      const data = await response.json();
      setAllIncome(data.content);
      const total = data.totalPages;

      setpageCount(total);
    };

    fetchData();
  }, [forceRender, limit]);

  const fetchIncome = async (currentPage) => {
    const res = await fetch(
      `http://localhost:8080/api/income/user?offset=${currentPage}&pageSize=${limit}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${currentUser.accessToken}`,
        },
      }
    );
    const data = await res.json();

    return data.content;
  };

  const handlePageClick = async (data) => {


    let currentPage = data.selected;

    const incomeFormServer = await fetchIncome(currentPage);

    setAllIncome(incomeFormServer);
    // scroll to the top
    //window.scrollTo(0, 0)
  };

  return (
    <>
      <div className="container-fluid budget__income sticky-config">
        <div className="container">
          <div className="row">
            <div className="col">
              <h2>Pajamos: {Math.round(incomeSum * 100) / 100} &euro;</h2>
            </div>
          </div>
        </div>
      </div>

      <div className="bottom">
        <div className="container">
          <div className="add">
            <div className="row text-center add__container">
              <form
                onSubmit={handleSubmit(onSubmit)}
                className="input-group my-3"
              >
                <input
                  {...register("incomeName", { required: true, minLength: 3, maxLength: 20 })}
                  type="text"
                  className="form-control add__description"
                  placeholder="Aprašymas"
                />

                <input
                  {...register("date", {
                    value: today,
                    required: true,
                    max: today,
                  })}
                  type="date"
                  className="form-control add__date"
                // placeholder="Data"
                />

                <input
                  {...register("amount", {
                    required: true,
                    min: 0.01,
                  })}
                  type="number"
                  className="form-control add__value"
                  placeholder="Suma"
                  step="0.01"
                />

                <div className="input-group-append">
                  <button className="btn" type="submit">
                    <FontAwesomeIcon
                      icon={faCirclePlus}
                      className="add__btn__income"
                    />
                  </button>
                </div>
              </form>
            </div>

            <div className="row ">
              <div className="col-sm-4 col-4">
                {errors?.incomeName?.type === "required" && (
                  <p>Šis laukas yra privalomas</p>
                )}
                {errors?.incomeName?.type === "minLength" && (
                  <p>Aprašymas turi būti bent 3 simbolių ilgio</p>
                )}
                {errors?.incomeName?.type === "maxLength" && (
                  <p>Aprašymas negali būti ilgesnis negu 10 simbolių</p>
                )}
              </div>
              <div className="col-sm-4 col-4">
                {errors?.date?.type === "required" && (
                  <p>Šis laukas yra privalomas</p>
                )}
                {errors?.date?.type === "max" && (
                  <p>Naujesnių nei šiandien įrašų negali būti</p>
                )}
              </div>
              <div className="col-sm-4 col-4">
                {errors?.amount?.type === "required" && (
                  <p>Šis laukas yra privalomas</p>
                )}
                {errors?.amount?.type === "min" && (
                  <p>Mažiausias įvestinų pajamų suma yra 0.01 &euro;</p>
                )}
              </div>
            </div>
          </div>
        </div>

        {/* <div className="mt-5 list"> */}
        <div className="container" style={{ paddingRight: 0 }}>
          <div
            className="col-12 income__list"
            style={{ paddingLeft: 0, paddingRight: 0 }}
          >
            <div className="income__list">
              <Table hover>
                <thead>
                  <tr>
                    <th>Aprašymas</th>
                    <th>Data</th>
                    <th>Suma</th>
                    <th></th>
                  </tr>
                </thead>
                <tbody>
                  {/* Display user's income on the page */}
                  {allIncome.map((income) => {
                    return (
                      // <div key={income.id}>
                      //     <div className='row'>
                      //         <div className='col-4' style={{ paddingLeft: 0 }}>
                      //             {income.incomeName}&nbsp;
                      //         </div>
                      //         <div className='col-3' style={{ paddingLeft: 0 }}>
                      //             {income.date}&nbsp;
                      //         </div>
                      //         <div className='col-3' style={{ paddingLeft: '6.5%' }}>
                      //             {income.amount}&euro;&nbsp;
                      //         </div>

                      //         <div className='col-2' style={{ textAlign: 'right', paddingLeft: 0, paddingRight: 0 }}>
                      //             <EditIncomeModal
                      //                 id={income.id}
                      //                 incomeName={income.incomeName}
                      //                 date={income.date}
                      //                 amount={income.amount}
                      //                 forceRender={forceRender}
                      //                 setForceRender={setForceRender}
                      //             />

                      //             <button
                      //                 onClick={() => showDeleteModal(income.id)}
                      //                 className="btn"
                      //                 type="button"
                      //                 style={{ paddingTop: 0, paddingBottom: 10 }}
                      //             >
                      //                 <FontAwesomeIcon
                      //                     icon="trash"
                      //                     className='add__btn__income'
                      //                     style={{ "width": "20px" }}
                      //                 />
                      //             </button>
                      //         </div>
                      //     </div>
                      // </div>

                      <tr key={uuid()}>
                        <td>{income.incomeName}&nbsp;</td>
                        <td>{income.date}&nbsp;</td>
                        <td>{income.amount}&euro;&nbsp;</td>

                        <td
                          style={{
                            textAlign: "right",
                            paddingLeft: 0,
                            paddingRight: 0,
                          }}
                        >
                          <EditIncomeModal
                            id={income.id}
                            incomeName={income.incomeName}
                            date={income.date}
                            amount={income.amount}
                            forceRender={forceRender}
                            setForceRender={setForceRender}
                          />

                          <button
                            onClick={() => showDeleteModal(income.id)}
                            className="btn"
                            type="button"
                            style={{ paddingTop: 0, paddingBottom: 10 }}
                          >
                            <FontAwesomeIcon
                              icon="trash"
                              className="add__btn__income"
                              style={{ width: "20px" }}
                            />
                          </button>
                        </td>
                      </tr>
                    );
                  })}
                </tbody>

                <DeleteModal
                  showModal={displayDeleteModal}
                  hideModal={hideDeleteModal}
                  confirmModal={removeIncome}
                  id={deleteId}
                />
              </Table>
            </div>
            {/* pagination for the user items */}
            {/* <ReactPaginate
              previousLabel={"previous"}
              nextLabel={"next"}
              breakLabel={"..."}
              pageCount={pageCount}
              marginPagesDisplayed={2}
              pageRangeDisplayed={3}
              onPageChange={handlePageClick}
              containerClassName={"pagination justify-content-center"}
              pageClassName={"page-item"}
              pageLinkClassName={"page-link"}
              previousClassName={"page-item"}
              previousLinkClassName={"page-link"}
              nextClassName={"page-item"}
              nextLinkClassName={"page-link"}
              breakClassName={"page-item"}
              breakLinkClassName={"page-link"}
              activeClassName={"active"}
            /> */}
            <Pagination
              pageCount={pageCount}
              onPageChange={handlePageClick} />
          </div>
        </div>
        {/* </div> */}
      </div>
    </>
  );
}
