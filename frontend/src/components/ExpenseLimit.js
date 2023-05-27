import React, { useState, useEffect } from "react";
import "./IncomeAndExpense.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCirclePlus } from "@fortawesome/free-solid-svg-icons";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import AuthService from "../services/auth.service";
import { useForm } from "react-hook-form";
import EditExpenseLimitModal from "./EditExpenseLimitModal";
import DeleteModal from "./DeleteModal";

import ReactPaginate from "react-paginate";
import Table from "react-bootstrap/Table";
import { Pagination } from "react-bootstrap";
import "./Pagination.css";

// This code copypasted from: https://codepen.io/fido123/pen/xzvxNw
// JavaScript is not included in this code, only html and css
export default function ExpenseLimit() {
  const [allExpenseLimit, setAllExpenseLimit] = useState([]);
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
    const response = await fetch("http://localhost:8080/api/limits/", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${currentUser.accessToken}`,
      },
      body: JSON.stringify({
        categoryId: data.categoryId,
        limit: data.amount,
      }),
    });

    if (response.status === 201) {
      successMessage("Pridėta");
      reset();
    } else {
      errorMessage("Klaida! Išliadų limitas šiai kategorijai jau nustatytas.");
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

  const removeExpenseLimit = async (id) => {
    const response = await fetch(`http://localhost:8080/api/limits/${id}`, {
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
  const [allExpenseLimit2, setAllExpenseLimit2] = useState([]);
  // Sums user's expense
  const expenseLimitsSum = allExpenseLimit2.reduce((n, { amount }) => n + amount, 0);
  useEffect(() => {
    const fetchData = async () => {
      const response = await fetch(
        `http://localhost:8080/api/limits/user/${currentUser.id}`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${currentUser.accessToken}`,
          },
        }
      );
      const data = await response.json();
      setAllExpenseLimit2(data);
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
        `http://localhost:8080/api/limits/user?offset=0&pageSize=${limit}`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${currentUser.accessToken}`,
          },
        }
      );
      const data = await response.json();
      setAllExpenseLimit(data.content);
      const total = data.totalPages;

      setpageCount(total);
    };

    fetchData();
  }, [forceRender, limit]);

  const fetchExpenseLimit = async (currentPage) => {
    const res = await fetch(
      `http://localhost:8080/api/limits/user?offset=${currentPage}&pageSize=${limit}`,
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
    console.log(data);

    let currentPage = data.selected;

    const expenseLimitFormServer = await fetchExpenseLimit(currentPage);

    setAllExpenseLimit(expenseLimitFormServer);
    // scroll to the top
    //window.scrollTo(0, 0)
  };

  return (
    <>


      <div className="container-fluid budget__expense sticky-config">
        <div className="container">
          <div className="row">
            <div className="col">
              <h2>Išlaidų limitų suma: {Math.round(expenseLimitsSum * 100) / 100} &euro;</h2>
            </div>
          </div>
        </div>
      </div>
      <div className="bottom ">
        <div className="container">
          <div className="add">
            <div className="row text-center add__container">
              <form
                onSubmit={handleSubmit(onSubmit)}
                className="col-12 col-sm-6 col-md-6 col-lg-6 input-group my-3"
              >

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
                    min: 1,
                  })}
                  type="number"
                  className="form-control add__value"
                  placeholder="Limitas"
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
                  <p>Mažiausias įvestinų išlaidų suma yra 0.01 &euro;</p>
                )}
              </div>
            </div>
          </div>
        </div>

        {/* <div className="mt-5 list"> */}
        <div className="container" style={{ paddingRight: 0 }}>
          <div
            className="col-12 expense"
            style={{ paddingLeft: 0, paddingRight: 0 }}
          >
            {/* <h2 className="expense__title">Limitai</h2> */}
            <div className="expense__list">
              <Table hover>
                <thead>
                  <tr>
                    {/* <th>Aprašymas</th>
                    <th>Data</th> */}
                    <th>Kategorija</th>
                    <th>Limitas</th>
                    <th></th>
                  </tr>
                </thead>
                <tbody>
                  {/* Display user's expense on the page */}
                  {allExpenseLimit.map((expenseLimit) => {
                    return (

                      <tr key={expenseLimit.id}>
                        <td>{expenseLimit.expensesCategory.name}&nbsp;</td>
                        <td>{expenseLimit.amount}&euro;&nbsp;</td>


                        <td

                          style={{
                            textAlign: "right",
                            paddingLeft: 0,
                            paddingRight: 0,
                          }}
                        >
                          <EditExpenseLimitModal
                            id={expenseLimit.id}
                            amount={expenseLimit.amount}
                            categoryId={expenseLimit.expensesCategory.id}
                            forceRender={forceRender}
                            setForceRender={setForceRender}
                            allCategory={allCategory}
                          />

                          <button
                            onClick={() => showDeleteModal(expenseLimit.id)}
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
                  confirmModal={removeExpenseLimit}
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
        </div>
        {/* </div> */}
      </div>
    </>
  );
}
