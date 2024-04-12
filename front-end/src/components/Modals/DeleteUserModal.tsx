import React, { useEffect, useState } from 'react';
import "../../App.css";
import axios from 'axios';

export const DeleteUserModal = () => {
    const [showModal, setShowModal] = useState(false);
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
    }, [showModal])

    const handleSubmit = (e) => {
        e.preventDefault();
        axios.delete(`http://localhost:8080/api/users/${user.id}`)
            .then(res => {
                console.log(res);
            })
            .catch(err => {
                console.log(err);
            })

        axios.delete(`http://localhost:8080/api/photos/${user.username}`)
            .then(res => {
                console.log(res);
                alert("User deleted successfully")
                toggleShowModal();
            })
            .catch(err => {
                console.log(err);
                alert("Error when deleting user")
                toggleShowModal();
            })
    }

    const toggleShowModal = () => {
        setUser({});
        setShowModal(!showModal);
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
                Delete User
            </button>

            {showModal && (
                <div className="modal">
                    <div onClick={toggleShowModal} className="overlay"></div>
                    <div className="modal-content">
                        <h2>Delete User</h2>
                        <select name="user" id="user" onChange={e => setUser(users[e.target.value])}>
                            <option value="" disabled selected></option>
                            {showUserOptions(users)}
                        </select> <br />
                        <button onClick={handleSubmit}>
                            Delete
                        </button>
                        <button className="close-modal" onClick={toggleShowModal}>
                            Close
                        </button>
                    </div>
                </div>
            )}
        </>
    )
}
