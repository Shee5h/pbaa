import React from 'react'
import { useForm } from "react-hook-form"
import AuthService from "../services/auth.service"
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import * as bootstrap from 'bootstrap';
import $ from "jquery";

export default function EditExpenseLimitModal({ id, categoryId, amount, forceRender, setForceRender, allCategory }) {
    const currentUser = AuthService.getCurrentUser();
    const { register, handleSubmit, formState: { errors } } = useForm({ mode: 'onSubmit', reValidateMode: 'onSubmit' });

    // This is used to figure out today's date, and format it accordingly
    let today = new Date();
    const dd = String(today.getDate()).padStart(2, '0');
    const mm = String(today.getMonth() + 1).padStart(2, '0');
    const yyyy = today.getFullYear();
    today = yyyy + '-' + mm + '-' + dd;

    const hideModal = () => {
        const myModalEl = document.getElementById('id' + id);
        const modal = bootstrap.Modal.getInstance(myModalEl);
        modal.hide();
    };

    const onSubmit = async (data) => {
        const response = await fetch(
            "http://localhost:8080/api/limits/",
            {
                method: "PUT",
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${currentUser.accessToken}`
                },
                body: JSON.stringify({
                    "id": id,
                    "categoryId": data.categoryId,
                    "limit": data.amount
                })
            }
        )

        if (response.status === 200) {
            successMessage();
            hideModal();
        }
        else {
            (errorMessage('Klaida! Išliadų limitas šiai kategorijai jau nustatytas.'))
        }

        setForceRender(!forceRender)
    }

    // Popup message configuration
    toast.configure()
    const successMessage = () => {
        toast.success('Pakeitimai išsaugoti', {
            position: toast.POSITION.TOP_CENTER,
            autoClose: 3000,
            theme: "colored",
            pauseOnHover: false,
            hideProgressBar: true
        })
    }
    const errorMessage = (msg) => {
        toast.error(msg, {
            position: toast.POSITION.TOP_CENTER,
            autoClose: 3000,
            theme: "colored",
            pauseOnHover: false,
            hideProgressBar: true
        })
    }

    return (
        <>
            <button
                type="button"
                className="btn"
                data-bs-toggle="modal"
                data-bs-target={"#id" + id}
                style={{ paddingTop: 0, paddingBottom: 10 }}
            >
                <FontAwesomeIcon icon="pen-to-square" className='add__btn__expense' />
            </button>

            <div
                className="modal"
                id={"id" + id}
                data-bs-backdrop="static"
                data-bs-keyboard="false"
                tabIndex="-1"
                aria-labelledby="staticBackdropLabel"
                aria-hidden="true"
            >
                <div className="modal-dialog modal-dialog-centered">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h5
                                className="modal-title"
                                id="staticBackdropLabel"
                            >
                                Įrašo redagavimas
                            </h5>
                            <button
                                type="button"
                                className="btn-close"
                                data-bs-dismiss="modal"
                                aria-label="Close">
                            </button>
                        </div>

                        <form onSubmit={handleSubmit(onSubmit)} className="modal-body">
                          
                            <select {...register("categoryId",
                                        {
                                            required: true,
                                        })
                                    }
                                    className="form-control add__description"
                                    type="text"
                                    placeholder="Kategorija"
                                    defaultValue={categoryId}
                                    >
                                    {allCategory.map((option) => (
                                    <option value={option.id}>{option.name}</option>
                                         ))}
                                </select>
                            <input
                                {...register("amount",
                                    {
                                        required: true,
                                        min: 1
                                    })
                                }
                                type="number"
                                className="form-control add__value mt-2"
                                placeholder="Kiekis"
                                step="0.01"
                                defaultValue={amount}
                            />
                            {errors?.amount?.type === "required" && <p>Laukas negali būti tuščias</p>}
                            {errors?.amount?.type === "min" && <p>Mažiausias įvestinų pajamų kiekis yra 1 &euro;</p>}

                            <div className="modal-footer">
                                <button
                                    type="button"
                                    className="btn btn-secondary"
                                    data-bs-dismiss="modal"
                                >
                                    Uždaryti
                                </button>
                                <button
                                    type="submit"
                                    className="btn btn-primary"                                  
                                >
                                    Išsaugoti
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </>
    )
}
