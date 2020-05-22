import React from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import * as SystemAction from '../../actions/System/System';
import Grid from "@material-ui/core/Grid";
import SlidePartner from "../SlideImg/SlidePartner";

const HomePage = () => {
    return (
        <Grid container>
            <Grid item xl={8} lg={8} md={8} sm={12} xs={12}>
                j
            </Grid>
            <Grid item xl={4} lg={4} md={4} sm={12} xs={12}>
                <SlidePartner/>
            </Grid>
        </Grid>
    );
};


const mapStateToProps = (state) => {
    return {User: state.User};
}

const mapDispatchToProps = (dispatch) => {
    return {
        startLoad: bindActionCreators(SystemAction.startLoad, dispatch)
    };
}


export default connect(mapStateToProps, mapDispatchToProps)(HomePage);
