import axios from "axios";
import { createContext, useContext, useState } from "react";

const AuthContext = createContext({});

const AuthProvider = ({children}) => {
    const [user, setUser] = useState(null);
    
    const loginAction = async (data) => {
        const res = await axios.get(`http://localhost:8080/api/users/user/${data.username}`);
        setUser(res.data);
    }

    const logOut = () => {
        setUser(null);
    }
    return <AuthContext.Provider value={{user, loginAction, logOut}}>{children}</AuthContext.Provider>;
};

export default AuthProvider;

export const userAuth = () => {
    return useContext(AuthContext);
};
