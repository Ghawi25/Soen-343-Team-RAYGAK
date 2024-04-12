import React, { useEffect, useState } from 'react';
import "../../App.css";
import axios from 'axios';

export const EditUserModal = () => {
    const [showModal, setShowModal] = useState(false);
    const [formData, setFormData] = useState({});
    const [image, setImage] = useState(null);
    const [users, setUsers] = useState([]);
    const [user, setUser] = useState({});

    useEffect(() => {
        axios.get(`http://localhost:8080/api/users`)
            .then(res => {
                setUsers(res.data);
                console.log(users)
            })
            .catch(err => {
                console.log(err);
            })
    }, [formData])

    const handleChange = (e: { target: { name: string; value: string; }; }) => {
        setFormData(({ ...formData, [e.target.name]: e.target.value }));
    }

    const handleImageChage = (e: any) => {
        setImage(e.target.files[0]);
    }

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log(user.id);
        axios.put(`http://localhost:8080/api/users/${user.id}`, formData)
            .then(res => {
                console.log(res);
            })
            .catch(err => {
                console.log(err);
            });

        axios.putForm(`http://localhost:8080/api/photos/${user.username}`, {
            username: formData.username,
            image: image
        })
            .then(res => {
                console.log(res);
                if (res.status == 200) {
                    alert("Successfully updated user");
                    toggleShowModal();
                } else {
                    alert("Error occured while updating user")
                    toggleShowModal();
                }
            })
            .catch(err => {
                console.log(err);
            });
    }

    const toggleShowModal = () => {
        setUser({});
        setFormData({})
;        setShowModal(!showModal);
    }

    const showUserOptions = (users: []) => {
        return users.map((user, index) => {
            return (
                <option value={index}>{user.username}</option>
            )
        })
    }

    return (
        <>
            <button onClick={toggleShowModal}>
                Edit User
            </button>

            {showModal && (
                <div className="modal">
                    <div onClick={toggleShowModal} className="overlay"></div>
                    <div className="modal-content">
                        <h2>Edit User</h2>
                        <select name="user" id="user" onChange={e => {
                            setUser(users[e.target.value]); 
                            setFormData({
                                username: user.username,
                                password: user.password,
                                userType: user.userType
                            });
                            console.log(formData);
                        }}>
                            <option value="" disabled selected></option>
                            {showUserOptions(users)}
                        </select> <br />
                        <form onSubmit={handleSubmit}>
                            <label htmlFor="username">Username:</label> <br />
                            <input type="text" name="username" id="username" required onChange={handleChange} defaultValue={user.username} /> <br />
                            <label htmlFor="password">Password:</label> <br />
                            <input type="text" onChange={handleChange} name="password" id="password" required defaultValue={user.password} /> <br />
                            <label htmlFor="userType">User type:</label> <br />
                            <select name="userType" id="userType" onChange={handleChange} required>
                                <option value="adult">Adult</option>
                                <option value="child">Child</option>
                                <option value="stranger">Stranger</option>
                            </select> <br />
                            <label htmlFor="image">Upload Profile Picture (.png):</label> <br />
                            <input type="file" accept="image/png" name="image" id="image" onChange={handleImageChage} required /> <br />
                            <input type="submit" value="Edit" />
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
