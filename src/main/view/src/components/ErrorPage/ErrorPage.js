import React from 'react';
import {makeStyles} from '@material-ui/core/styles';
import Container from "@material-ui/core/Container";
import Card from "@material-ui/core/Card";
import CardActions from '@material-ui/core/CardActions';
import CardContent from '@material-ui/core/CardContent';
import Typography from "@material-ui/core/Typography";
import {Link} from "react-router-dom";
import _ from "lodash";
import Button from "@material-ui/core/Button";

const useStyles = makeStyles({
    root: {
        marginTop: 12,
    }
});

const urlParams = new URLSearchParams(window.location.search);
const ErrorPage = () => {
    const classes = useStyles();
    return (
        <Container>
            <Card className={classes.root}>
                <CardContent>
                    {
                        _.isString(urlParams.get("error")) ? (<>
                            <Typography variant="h6">
                                Ops =) Something went Wrong!
                            </Typography>
                            <Typography variant="h4">
                                {urlParams.get("error")}
                            </Typography>
                        </>) : (<>
                            <Typography variant="h6">
                                Don't worry all is well.
                            </Typography>
                        </>)
                    }

                </CardContent>
                <CardActions>
                    <Button component={Link} to="/" variant="contained" color="primary">
                        Go home page
                    </Button>
                </CardActions>
            </Card>
        </Container>
    );
};

export default ErrorPage;