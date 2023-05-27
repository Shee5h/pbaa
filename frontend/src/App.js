import React, { useState } from "react";
import { Routes, Route } from "react-router-dom";
import Login from "./components/Login";
import Register from "./components/Register";
import Home from "./components/Home";
import Profile from "./components/Profile";
import BoardUser from "./components/BoardUser";
// import BoardModerator from "./components/BoardModerator";
import BoardAdmin from "./components/BoardAdmin";
import HomeLoggedIn from "./components/HomeLoggedIn";
import Income from "./components/Income";
import Expense from "./components/Expense";
import ExpenseLimit from "./components/ExpenseLimit";
import Category from "./components/Category";
import "./components/FontAwesomeIcon/Icons";
import './App.css';
import Navbar from "./components/Navbar";
import AboutUs from "./components/AboutUs";
import Contacts from "./components/Contacts.js";
import Blog from "./components/Blog.js";
import { RenderContext } from './components/RenderContext';
import ErrorPage from "./components/ErrorPage";
import Users from "./components/Users";
import Footer from "./components/Footer";
const App = () => {
  const [render, setRender] = useState(false);

  return (
    <>
      {/* This shows when the screen goes small and user clicks button to expand */}
      <div className="site-mobile-menu site-navbar-target">
        <div className="site-mobile-menu-header">
          <div className="site-mobile-menu-close mt-3">
            <span className="icon-close2 js-menu-toggle"></span>
          </div>
        </div>
        <div className="site-mobile-menu-body"></div>
      </div>

      <RenderContext.Provider value={{ render, setRender }}>
        <header>
          <Navbar />
        </header>

        <main>
          <Routes>
            <Route path={"/"} element={<Home />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            <Route path="/profile" element={<Profile />} />
            <Route path="/user" element={<BoardUser />} />
            <Route path="/users" element={<Users />} />
            {/* <Route path="/mod" element={<BoardModerator />} /> */}
            <Route path="/admin" element={<BoardAdmin />} />
            {/* <Route path="/register-success" element={<RegisterSuccess />} /> */}
            <Route path="/statistics" element={<HomeLoggedIn />} />
            <Route path="/income" element={<Income />} />
            <Route path="/expense" element={<Expense />} />
            <Route path="/limits" element={<ExpenseLimit />} />
            <Route path="/category" element={<Category />} />
            <Route path="/aboutus" element={<AboutUs />} />
            <Route path="/contacts" element={<Contacts />} />
            <Route path="/blog" element={<Blog />} />
            <Route path="*" element={<ErrorPage />} />
          </Routes>
        </main>
      </RenderContext.Provider>

      {/* Commented the footer section for now */}
      {/* <footer>
        <Footer />
      </footer> */}
    </>
  );
};
export default App;
