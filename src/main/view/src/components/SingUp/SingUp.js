import React, {useState} from 'react';
import Container from "@material-ui/core/Container";
import Card from "@material-ui/core/Card";
import CardContent from "@material-ui/core/CardContent";
import {makeStyles} from "@material-ui/core/styles";
import Typography from "@material-ui/core/Typography";
import Link from '@material-ui/core/Link';
import Button from "@material-ui/core/Button";
import FormControl from "@material-ui/core/FormControl";
import InputLabel from "@material-ui/core/InputLabel";
import Input from "@material-ui/core/Input";
import FormHelperText from "@material-ui/core/FormHelperText";
import ImageUploader from 'react-images-upload';

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
    formSingUp: {
        marginTop: 15
    },
    formControl: {
        margin: "15px 0"
    }
});

const SingUp = () => {
    const classes = useStyles();
    const [name, setName] = useState("");
    const [nameError, setNameError] = useState(false);

    const [email, setEmail] = useState("");
    const [emailError, setEmailError] = useState(false);

    const [pictures, setPictures] = useState([]);
    const [errorPictures, setErrorPictures] = useState(false)

    const validateEmail = emailVal => {
        // eslint-disable-next-line
        let re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        return re.test(String(emailVal).toLowerCase());
    }
    const onDrop = (pictureFiles) => {
        setPictures(pictureFiles);
        setErrorPictures(false);
    }

    const onSubmit = e => {
        e.preventDefault();
        let validEmail = validateEmail(email);
        if (!validEmail) {
            setEmailError(true);
        }
        let validName = name.length > 4
        if (!validName) {
            setNameError(true);
        }
        let validPicture = pictures.length === 1;
        if (!validPicture) {
            setErrorPictures(true);
        }
        if (validPicture && validName && validEmail) {
            console.log("submit");
        }
    }
    return (
        <Container>
            <Card className={classes.root}>
                <CardContent>
                    <Typography variant={"h5"}>
                        Create your account
                    </Typography>
                    <Button className={classes.googleButton} component={Link} fullWidth={true}
                            href={'https://accounts.google.com/o/oauth2/auth?scope=profile email&response_type=code&access_type=offline&redirect_uri=http://localhost:8080/api/v0/google&client_id=206183164477-qeh7n71mhlf4au9f236fc1i8tr62r080.apps.googleusercontent.com'}
                            variant="contained" color="primary">
                        Sing Up with Google
                    </Button>
                    <form onSubmit={onSubmit} className={classes.formSingUp} noValidate={false} autoComplete="off">
                        <FormControl variant="outlined" className={classes.formControl} fullWidth={true}
                                     error={emailError}>
                            <InputLabel htmlFor={"enter-email"}>Enter name</InputLabel>
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
                                     error={nameError}>
                            <InputLabel htmlFor={"enter-name"}>Enter name</InputLabel>
                            <Input

                                id={"enter-name"}
                                value={name}
                                onChange={e => {
                                    setName(e.target.value);
                                    setNameError(false)
                                }}
                                aria-describedby={"enter-name-helper"}
                            />
                            <FormHelperText id={"enter-name-helper"}>{nameError && "Name is require"}</FormHelperText>
                        </FormControl>
                        <FormControl variant="outlined" className={classes.formControl} fullWidth={true}
                                     error={errorPictures}>
                            <ImageUploader
                                withPreview={true}
                                withIcon={true}
                                buttonText='Choose avatar'
                                fileSizeError={"File size is too big"}
                                fileTypeError={"Is not supported file extension"}
                                onChange={onDrop}
                                imgExtension={['.jpg', '.gif', '.png', '.gif']}
                                maxFileSize={5242880}
                                singleImage={true}
                            />
                            <FormHelperText
                                id={"enter-name-helper"}>{errorPictures && "Avatar is require"}</FormHelperText>
                        </FormControl>

                        <Button type={"submit"} fullWidth={true} variant="contained" color="primary">Sing Up</Button>
                    </form>
                </CardContent>
            </Card>
        </Container>
    );
};

export default SingUp;