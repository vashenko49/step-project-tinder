import React, {useState} from 'react';
import Container from "@material-ui/core/Container";
import Card from "@material-ui/core/Card";
import CardContent from "@material-ui/core/CardContent";
import {makeStyles} from "@material-ui/core/styles";
import Typography from "@material-ui/core/Typography";
import Button from "@material-ui/core/Button";
import Link from "@material-ui/core/Link";
import Oauth from '../../util/Oauth';
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import * as SystemAction from "../../actions/System/System";
import _ from 'lodash';
import FormControl from "@material-ui/core/FormControl";
import InputLabel from "@material-ui/core/InputLabel";
import Input from "@material-ui/core/Input";
import FormHelperText from "@material-ui/core/FormHelperText";
import InputAdornment from "@material-ui/core/InputAdornment";
import IconButton from "@material-ui/core/IconButton";
import {Visibility, VisibilityOff} from "@material-ui/icons";
import * as UserAction from "../../actions/User/User";
import {objectToFormData} from 'object-to-formdata';

const useStyles = makeStyles({
    root: {
        margin: "12px auto",
        maxWidth: "478px",
        padding: "24px"
    },
    googleButton: {
        marginTop: 15,
        textDecoration: "none"
    },
    formSingIn: {
        marginTop: 15
    },
    formControl: {
        margin: "15px 0"
    }
});


const urlParams = new URLSearchParams(window.location.search);
const SingIn = ({startLoad, signInUser, history}) => {
    const [email, setEmail] = useState("");
    const [emailError, setEmailError] = useState(false);
    const [password, setPassword] = useState("");
    const [passwordError, setPasswordError] = useState(false);
    const [showPassword, setShowPassword] = useState(false);

    const validateEmail = Oauth.validateEmail;
    const handleClickShowPassword = () => {
        setShowPassword(!showPassword);
    }

    const handleMouseDownPassword = e => e.preventDefault();
    const onSubmit = e => {
        e.preventDefault();
        let validEmail = validateEmail(email);
        if (!validEmail) {
            setEmailError(true);
        }
        let validPassword = password.length > 4;
        if (!validPassword) {
            setPasswordError(true);
        }
        if (validPassword && validEmail) {
            const options = {
                indices: true,
                nullsAsUndefineds: true
            };
            const formData = objectToFormData({
                email: email,
                password: password
            }, options);

            signInUser(formData, ()=>{
                history.push("/")
            });
        }
    };
    const classes = useStyles();


    return (
        <Container>
            {_.isString(urlParams.get("message")) && <Typography align={'center'} variant="h6" color={"secondary"}>
                {urlParams.get("message")}
            </Typography>}
            <Card className={classes.root}>
                <CardContent>
                    <Typography variant={"h5"}>
                        Sing in your account
                    </Typography>
                    <Button onClick={startLoad} className={classes.googleButton} component={Link} fullWidth={true}
                            href={'https://accounts.google.com/o/oauth2/auth?scope=email&response_type=code&access_type=offline&redirect_uri=http://localhost:8080/api/v0/google-sign-in&client_id=206183164477-qeh7n71mhlf4au9f236fc1i8tr62r080.apps.googleusercontent.com'}
                            variant="contained" color="primary">
                        Sing In with Google
                    </Button>
                    <form onSubmit={onSubmit} className={classes.formSingUp} noValidate={false} autoComplete="off">
                        <FormControl variant="outlined" className={classes.formControl} fullWidth={true}
                                     error={emailError}>
                            <InputLabel htmlFor={"enter-email"}>Enter email</InputLabel>
                            <Input
                                id={"enter-email"}
                                value={email}
                                onChange={e => {
                                    setEmail(e.target.value);
                                    setEmailError(false)
                                }}
                                aria-describedby={"enter-email-helper"}
                            />
                            <FormHelperText
                                id={"enter-email-helper"}>{emailError && "Email is require"}</FormHelperText>
                        </FormControl>
                        <FormControl variant="outlined" className={classes.formControl} fullWidth={true}
                                     error={passwordError}>
                            <InputLabel htmlFor={"enter-password"}>Enter password</InputLabel>
                            <Input
                                id={"enter-password"}
                                value={password}
                                onChange={e => {
                                    setPassword(e.target.value);
                                    setPasswordError(false)
                                }}
                                aria-describedby={"enter-password-helper"}
                                type={showPassword ? 'text' : 'password'}
                                endAdornment={
                                    <InputAdornment position="end">
                                        <IconButton
                                            aria-label="toggle password visibility"
                                            onClick={handleClickShowPassword}
                                            onMouseDown={handleMouseDownPassword}
                                        >
                                            {showPassword ? <Visibility/> : <VisibilityOff/>}
                                        </IconButton>
                                    </InputAdornment>
                                }
                            />
                            <FormHelperText
                                id={"enter-password-helper"}>{passwordError && "Password is require"}</FormHelperText>
                        </FormControl>
                        <Button type={"submit"} fullWidth={true} variant="contained" color="primary">Sing In</Button>
                    </form>
                </CardContent>
            </Card>
        </Container>
    );
};


const mapDispatchToProps = (dispatch) => {
    return {
        startLoad: bindActionCreators(SystemAction.startLoad, dispatch),
        signInUser: bindActionCreators(UserAction.signInUser, dispatch)
    };
}


export default connect(null, mapDispatchToProps)(SingIn);