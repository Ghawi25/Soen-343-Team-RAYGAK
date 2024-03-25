import { createContext, useContext } from "react";

const AuthContext = createContext({});

const AuthProvider = ({children}) => {
    return <AuthContext.Provider>{children}</AuthContext.Provider>;
};

export default AuthProvider;

export const userAuth = () => {
    return useContext(AuthContext);
};
