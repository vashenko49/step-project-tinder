import React from 'react';
import { Route, Redirect } from 'react-router-dom';
import { connect } from 'react-redux';

const PrivateRoute = ({ component: Component, User:{isAuthorization}, ...rest }) => {
    return (
        <Route
            {...rest}
            render={props => (
                isAuthorization
                    ? <Component {...props} />
                    : <Redirect to="/" />
            )}
        />
    );
};

const mapStateToProps = (state)=> {
    return { User: state.User };
}

export default connect(mapStateToProps, null)(PrivateRoute);
