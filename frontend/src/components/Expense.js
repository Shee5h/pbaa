import React, { useState, useEffect } from "react";
import "./IncomeAndExpense.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCirclePlus } from "@fortawesome/free-solid-svg-icons";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import AuthService from "../services/auth.service";
import { useForm } from "react-hook-form";
import EditExpenseModal from "./EditExpenseModal";
import DeleteModal from "./DeleteModal";

import ReactPaginate from "react-paginate";
import Table from "react-bootstrap/Table";
import { Pagination } from "react-bootstrap";
import ReactCSV from "./ReactCSV";
import Accordion from 'react-bootstrap/Accordion'
import "./Pagination.css";
import uuid from "react-uuid";

// This code copypasted from: https://codepen.io/fido123/pen/xzvxNw
// JavaScript is not included in this code, only html and css
export default function Expense() {
  const [allExpense, setAllExpense] = useState([]);
  const [allCategory, setAllCategory] = useState([]);
  const [forceRender, setForceRender] = useState(false);
  const [displayDeleteModal, setDisplayDeleteModal] = useState(false);
  const [deleteId, setDeleteId] = useState();
  const currentUser = AuthService.getCurrentUser();
  const {
    register,
    handleSubmit,
    formState: { errors },
    reset,
  } = useForm({ mode: "onSubmit", reValidateMode: "onSubmit" });


  // This is used to figure out today's date, and format it accordingly
  let today = new Date();
  const dd = String(today.getDate()).padStart(2, "0");
  const mm = String(today.getMonth() + 1).padStart(2, "0");
  const yyyy = today.getFullYear();
  today = yyyy + "-" + mm + "-" + dd;

  useEffect(() => {
    const fetchCategoryData = async () => {
      const categoryResponse = await fetch(
        `http://localhost:8080/api/categories`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${currentUser.accessToken}`,
          },
        }
      );
      const categoryData = await categoryResponse.json();
      setAllCategory(categoryData);
    };
    fetchCategoryData();
  }, [forceRender]);

  // Add user's expense to database from the inputs
  const onSubmit = async (data) => {
    const response = await fetch("http://localhost:8080/api/expense/", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${currentUser.accessToken}`,
      },
      body: JSON.stringify({
        expenseName: data.expenseName,
        categoryId: data.categoryId,
        date: data.date,
        amount: data.amount,
      }),
    });

    if (response.status === 201) {
      successMessage("Pridėta");
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

  const removeExpense = async (id) => {
    const response = await fetch(`http://localhost:8080/api/expense/${id}`, {
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

  // Fetch all user's expense from database to display
  const [allExpense2, setAllExpense2] = useState([]);
  // Sums user's expense
  const expenseSum = allExpense2.reduce((n, { amount }) => n + amount, 0);
  useEffect(() => {
    const fetchData = async () => {
      const response = await fetch(
        `http://localhost:8080/api/expense/user/${currentUser.id}`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${currentUser.accessToken}`,
          },
        }
      );
      const data = await response.json();
      setAllExpense2(data);
    };
    fetchData();
  }, [forceRender]);


  //pagination..........................

  const [pageCount, setpageCount] = useState(0);
  //limit of how many items per page to see
  let limit = 8;

  useEffect(() => {
    const fetchData = async () => {
      const response = await fetch(
        `http://localhost:8080/api/expense/user?offset=0&pageSize=${limit}`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${currentUser.accessToken}`,
          },
        }
      );
      const data = await response.json();
      setAllExpense(data.content);
      const total = data.totalPages;

      setpageCount(total);
    };

    fetchData();
  }, [forceRender, limit]);

  const fetchExpense = async (currentPage) => {
    const res = await fetch(
      `http://localhost:8080/api/expense/user?offset=${currentPage}&pageSize=${limit}`,
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

    const expenseFormServer = await fetchExpense(currentPage);

    setAllExpense(expenseFormServer);
    // scroll to the top
    //window.scrollTo(0, 0)
  };


  //irasu filtravimas

  let today2 = new Date();
  const dd2 = String(today2.getDate()).padStart(2, "0") - 1;
  const mm2 = String(today2.getMonth() + 1).padStart(2, "0");
  const yyyy2 = today2.getFullYear();
  today2 = yyyy2 + "-" + mm2 + "-" + dd2;


  let date2 = today; // todays date
  let date1 = today2; // yesterdays date

  //let category = null;

  const onSubmit1 = async (fr) => {
    if (fr.date1 == "" && fr.date2 == "") {
      const res = await fetch(
        `http://localhost:8080/api/expense/userCategory?category=${fr.category}&offset=0&pageSize=${limit}`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${currentUser.accessToken}`,
          },
        }
      );
      const data = await res.json();
      setAllExpense(data.content);
      const total = data.totalPages;

      setpageCount(total);
      return data.content;
    }
    else {
      const res = await fetch(
        `http://localhost:8080/api/expense/userDate?date1=${fr.date1}&date2=${fr.date2}&category=${fr.category}&offset=0&pageSize=${limit}`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${currentUser.accessToken}`,
          },
        }
      );
      const data = await res.json();
      setAllExpense(data.content);
      const total = data.totalPages;

      setpageCount(total);

      return data.content;
    }
  };



  const onSubmit2 = async (date1, date2, category, currentPage) => {
    const res = await fetch(
      `http://localhost:8080/api/expense/userDate?date1=${date1}&date2=${date2}&category=${category}&offset=${currentPage}&pageSize=${limit}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${currentUser.accessToken}`,
        },
      }
    );
    const data = await res.json();
    console.log(data.content);
    return data.content;
  };

  const fetchExpenseWithCategory = async (category, currentPage) => {
    const res = await fetch(
      `http://localhost:8080/api/expense/userCategory?category=${category}&offset=${currentPage}&pageSize=${limit}`,
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

  const {
    register: register2,
    handleSubmit: handleSubmit2,
    formState: { errors: errors2 }
    //reset,
  } = useForm({ mode: "onSubmit", reValidateMode: "onSubmit" });

  //const [allExpense, setAllExpense] = useState([]);

  return (
    <>


      <div className="container-fluid budget__expense sticky-config">
        <div className="container">
          <div className="row">
            <div className="col">
              <h2>Išlaidos: {Math.round(expenseSum * 100) / 100} &euro;</h2>
            </div>
          </div>
        </div>
      </div>

      <div className="bottom ">
        <div className="container">
          <div >
            <Accordion defaultActiveKey="0" >
              <Accordion.Item eventKey="0" >
                <Accordion.Header >Naujas įrašas</Accordion.Header>
                <Accordion.Body >
                  <div className="add">
                    <div className="row text-center add__container">
                      <form
                        onSubmit={handleSubmit(onSubmit)}
                        className="col-12 col-sm-6 col-md-6 col-lg-6 input-group my-3"
                      >
                        <input
                          {...register("expenseName", { required: true, minLength: 3, maxLength: 20 })}
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
                          placeholder="Data"
                        />

                        <select
                          {...register("categoryId", {
                            required: true,
                          })}
                          className="form-control add__description"
                          type="text"
                        >
                          <option value={""}>--Pasirinkite kategoriją--</option>
                          {allCategory.map((option) => (
                            <option value={option.id}>{option.name}</option>
                          ))}
                        </select>

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
                              className="add__btn__expense"
                            />
                          </button>
                        </div>
                      </form>
                    </div>
                    <div className="row ">
                      <div className="col-sm-3 col-3">
                        {errors?.expenseName?.type === "required" && (
                          <p>Šis laukas yra privalomas</p>
                        )}
                        {errors?.expenseName?.type === "minLength" && (
                          <p>Aprašymas turi būti bent 3 simbolių ilgio</p>
                        )}
                        {errors?.incomeName?.type === "maxLength" && (
                          <p>Aprašymas negali būti ilgesnis negu 10 simbolių</p>
                        )}
                      </div>
                      <div className="col-sm-3 col-3">
                        {errors?.date?.type === "required" && (
                          <p>Šis laukas yra privalomas</p>
                        )}
                        {errors?.date?.type === "max" && (
                          <p>Naujesnių nei šiandien įrašų negali būti</p>
                        )}
                      </div>
                      <div className="col-sm-3 col-3">
                        {errors?.categoryId?.type === "required" && (
                          <p>Šis laukas yra privalomas</p>
                        )}
                        {errors?.categoryId?.type === "minLength" && (
                          <p>Aprašymas turi būti bent 4 simbolių ilgio</p>
                        )}
                      </div>

                      <div className="col-sm-3 col-3">
                        {errors?.amount?.type === "required" && (
                          <p>Šis laukas yra privalomas</p>
                        )}
                        {errors?.amount?.type === "min" && (
                          <p>Mažiausia įvestinų išlaidų suma yra 0.01 &euro;</p>
                        )}
                      </div>
                    </div>
                  </div>
                </Accordion.Body>
              </Accordion.Item>
              <Accordion.Item eventKey="1">
                <Accordion.Header>Įrašų filtravimas</Accordion.Header>
                <Accordion.Body>
                  <div className="add">
                    <div className="row text-center add__container">
                      <form
                        onSubmit={handleSubmit2(onSubmit1)}
                        className="col-12 col-sm-6 col-md-6 col-lg-6 input-group my-3"
                      >
                        <input
                          {...register2("date1", {
                            //value: today2,
                            //required: true,
                            max: today,
                          })}
                          type="date"
                          className="form-control add__date"
                          placeholder="Data"
                        />

                        <input
                          {...register2("date2", {
                            //value: today,
                            //required: true,
                            max: today,
                          })}
                          type="date"
                          className="form-control add__date"
                          placeholder="Data"
                        />

                        <select
                          {...register2("category", {
                            required: true,
                          })}
                          className="form-control add__description"
                          type="text"
                        >
                          <option value={""}>--Pasirinkite kategoriją--</option>
                          {allCategory.map((option) => (
                            <option value={option.name}>{option.name}</option>
                          ))}
                        </select>



                        <div className="input-group-append">
                          <button className="btn" type="submit">
                            <FontAwesomeIcon
                              icon={faCirclePlus}
                              className="add__btn__expense"
                            />
                          </button>
                        </div>
                      </form>

                    </div>

                    <div className="row ">
                      <div className="col-sm-4 col-4">
                        {errors2?.date1?.type === "required" && (
                          <p>Šis laukas yra privalomas</p>
                        )}
                        {errors2?.date1?.type === "max" && (
                          <p>Naujesnių nei šiandien įrašų negali būti</p>
                        )}
                      </div>
                      <div className="col-sm-4 col-4">
                        {errors2?.date2?.type === "required" && (
                          <p>Šis laukas yra privalomas</p>
                        )}
                        {errors2?.date2?.type === "max" && (
                          <p>Naujesnių nei šiandien įrašų negali būti</p>
                        )}
                      </div>
                      <div className="col-sm-4 col-4">
                        {errors2?.category?.type === "required" && (
                          <p>Šis laukas yra privalomas</p>
                        )}
                        {errors2?.category?.type === "minLength" && (
                          <p>Aprašymas turi būti bent 4 simbolių ilgio</p>
                        )}
                      </div>


                    </div>
                  </div>
                </Accordion.Body>
              </Accordion.Item>
            </Accordion>
          </div>
        </div>

        {/* <div className="mt-5 list"> */}
        <div className="container" style={{ paddingRight: 0 }}>
          <div
            className="col-12 expense"
            style={{ paddingLeft: 0, paddingRight: 0 }}
          >
            {/* <h2 className="expense__title">Išlaidos</h2> */}
            <div className="expense__list">
              <Table hover>
                <thead>
                  <tr>
                    <th>Aprašymas</th>
                    <th>Data</th>
                    <th>Kategorija</th>
                    <th>Suma</th>
                    <th></th>
                  </tr>
                </thead>
                <tbody>
                  {/* Display user's expense on the page */}
                  {allExpense.map((expense) => {
                    return (
                      // <div key={expense.id}>
                      //   <div className="row">
                      //     <div className="col-3" style={{ paddingLeft: 0 }}>
                      //       {expense.expenseName}&nbsp;
                      //     </div>
                      //     <div className="col-3" style={{ paddingLeft: 0 }}>
                      //       {expense.date}&nbsp;
                      //     </div>

                      //     <div className="col-2" style={{ paddingLeft: 0 }}>
                      //       {expense.expensesCategory.name}&nbsp;
                      //     </div>
                      //     <div className="col-2" style={{ paddingLeft: "6%" }}>
                      //       {expense.amount}&euro;&nbsp;
                      //     </div>

                      //     <div
                      //       className="col-2"
                      //       style={{
                      //         textAlign: "right",
                      //         paddingLeft: 0,
                      //         paddingRight: 0,
                      //       }}
                      //     >
                      //       <EditExpenseModal
                      //         id={expense.id}
                      //         expenseName={expense.expenseName}
                      //         date={expense.date}
                      //         amount={expense.amount}
                      //         category={expense.expensesCategory.name}
                      //         forceRender={forceRender}
                      //         setForceRender={setForceRender}
                      //         allCategory={allCategory}
                      //       />

                      //       <button
                      //         onClick={() => showDeleteModal(expense.id)}
                      //         className="btn"
                      //         type="button"
                      //         style={{ paddingTop: 0, paddingBottom: 10 }}
                      //       >
                      //         <FontAwesomeIcon
                      //           icon="trash"
                      //           className="add__btn__expense"
                      //           style={{ width: "20px" }}
                      //         />
                      //       </button>
                      //     </div>
                      //   </div>
                      // </div>
                      <tr key={uuid()}>
                        <td>{expense.expenseName}&nbsp;</td>
                        <td>{expense.date}&nbsp;</td>
                        <td>{expense.expensesCategory.name}&nbsp;</td>
                        <td>{expense.amount}&euro;&nbsp;</td>


                        <td

                          style={{
                            textAlign: "right",
                            paddingLeft: 0,
                            paddingRight: 0,
                          }}
                        >
                          <EditExpenseModal
                            id={expense.id}
                            expenseName={expense.expenseName}
                            date={expense.date}
                            amount={expense.amount}
                            categoryId={expense.expensesCategory.id}
                            forceRender={forceRender}
                            setForceRender={setForceRender}
                            allCategory={allCategory}
                          />

                          <button
                            onClick={() => showDeleteModal(expense.id)}
                            className="btn"
                            type="button"
                            style={{ paddingTop: 0, paddingBottom: 10 }}
                          >
                            <FontAwesomeIcon
                              icon="trash"
                              className="add__btn__expense"
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
                  confirmModal={removeExpense}
                  id={deleteId}
                />
              </Table>
            </div>

            {/* pagination for the user items */}



            <ReactPaginate
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
            />


          </div>
          {/* This section is CSV export button */}
          <button type="submit" className="btn btn-lg" style={{ float: "right", backgroundColor: "#008F8C" }}>
            <ReactCSV allExpenses={allExpense2} />
          </button>

        </div>
        {/* </div> */}
      </div>
    </>
  );
}
