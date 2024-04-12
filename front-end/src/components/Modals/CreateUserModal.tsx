import React, { useState } from 'react'
import "../../App.css"
import axios from 'axios';

export const CreateUserModal = () => {
    const [showModal, setShowModal] = useState(false);
    const [formData, setFormData] = useState({});
    const [image, setImage] = useState(null);
    
    const handleChange = (e: { target: { name: string; value: string; }; }) => {
        setFormData(({...formData, [e.target.name]: e.target.value}));        
    }

    const handleImageChage = (e: any) => {
        setImage(e.target.files[0]);
    }

    const handleSubmit = (e) => {
        e.preventDefault();
        axios.post(`http://localhost:8080/api/users`, formData)
        .then(res => {
            console.log(res);
        })
        .catch(err => {
            console.log(err);
        });

        axios.postForm(`http://localhost:8080/api/photos`, {
            username: formData.username,
            image: image
        })
        .then(res => {
            console.log(res);
            if(res.status == 200) {
                alert("Successfully created new user");
                toggleShowModal();
            } else  {
                alert("Error occured while creating new user")
                toggleShowModal();
            }
        })
        .catch(err => {
            console.log(err);
        });
    }

    const toggleShowModal = () => {
        setShowModal(!showModal);
    }

    return (
        <>
            <button onClick={toggleShowModal}>
                Create User
            </button>

            {showModal && (
                <div className="modal">
                    <div onClick={toggleShowModal} className="overlay"></div>
                    <div className="modal-content">
                        <h2>Create User</h2>
                        <form onSubmit={handleSubmit}>
                            <label htmlFor="username">Username:</label> <br />
                            <input type="text" name="username" id="username" required onChange={handleChange}/> <br />
                            <label htmlFor="password">Password:</label> <br />
                            <input type="password" onChange={handleChange} name="password" id="password" required/> <br />
                            <label htmlFor="userType">User type:</label> <br />
                            <select name="userType" id="userType" onChange={handleChange} required>
                                <option value="" disabled selected></option>
                                <option value="adult">Adult</option>
                                <option value="child">Child</option>
                                <option value="stranger">Stranger</option>
                            </select> <br />
                            <label htmlFor="image">Upload Profile Picture (.png):</label> <br />
                            <input type="file" accept="image/png" name="image" id="image" onChange={handleImageChage} required/> <br />
                            <input type="submit" value="Create" />
                        </form>
                        <button className="close-modal" onClick={toggleShowModal}>
                            Close
                        </button>
                    </div>
                </div>
            )}
        </>
    )
}
