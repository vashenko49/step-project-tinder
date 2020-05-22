import React, {useEffect, useState} from 'react';
import Container from "@material-ui/core/Container";
import Grid from "@material-ui/core/Grid";
import {makeStyles} from "@material-ui/core/styles";
import Card from "@material-ui/core/Card";
import CardContent from "@material-ui/core/CardContent";
import Typography from "@material-ui/core/Typography";
import FormControl from "@material-ui/core/FormControl";
import ImageUploader from "react-images-upload";
import FormHelperText from "@material-ui/core/FormHelperText";
import Button from "@material-ui/core/Button";
import InputLabel from "@material-ui/core/InputLabel";
import Input from "@material-ui/core/Input";
import InputAdornment from "@material-ui/core/InputAdornment";
import IconButton from "@material-ui/core/IconButton";
import {Visibility, VisibilityOff} from "@material-ui/icons";
import {connect} from "react-redux";
import _ from 'lodash';
import TextField from "@material-ui/core/TextField";
import FormLabel from "@material-ui/core/FormLabel";
import RadioGroup from "@material-ui/core/RadioGroup";
import Radio from "@material-ui/core/Radio";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import Slider from "@material-ui/core/Slider";
import {bindActionCreators} from "redux";
import * as UserAction from "../../actions/User/User";
import {objectToFormData} from 'object-to-formdata';

const useStyles = makeStyles({
    root: {
        marginTop: "10px"
    },
    card: {
        padding: "15px",
        margin: 10
    },
    formControl: {
        margin: "15px 0"
    }
});


const PersonalCabinet = ({User: {first_name, age, interests, gender, genderpartner, aboutMe, max_distance, min_age, max_age, imagesList}, editData, changePassword, changeImg}) => {
    const classes = useStyles();
    const [newFirstName, setNewFirstName] = useState(first_name);
    const [nameError, setNameError] = useState(false);
    const [newAge, setNewAge] = useState((_.isNull(age) || age < 18) ? 18 : age);
    const [newInterests, setNewInterests] = useState(_.isNull(interests) ? "" : interests);
    const [newGander, setNewGander] = useState(_.isNull(gender) ? "male" : gender);
    const [newGanderPartner, setNewGanderPartner] = useState(_.isNull(genderpartner) ? "male" : genderpartner);
    const [newAboutMe, setNewAboutMe] = useState(_.isNull(aboutMe) ? "" : aboutMe);
    const [newMaxDistance, setNewMaxDistance] = useState(_.isNull(max_distance) ? "" : max_distance);
    const [newMinAge, setNewMinAge] = useState(_.isNull(min_age) ? "" : min_age);
    const [newMaxAge, setNewMaxAge] = useState(_.isNull(max_age) ? "" : max_age);

    useEffect(() => {

    }, [])

    const [pictures, setPictures] = useState([]);
    const [errorPictures, setErrorPictures] = useState(false)

    const [password, setPassword] = useState("");
    const [passwordError, setPasswordError] = useState(false);
    const [showPassword, setShowPassword] = useState(false);

    const [newPassword, setNewPassword] = useState("");
    const [newPasswordError, setNewPasswordError] = useState(false);
    const [showNewPassword, setShowNewPassword] = useState(false);


    const handleMouseDownPassword = e => e.preventDefault();
    const handleClickShowPassword = () => {
        setShowPassword(!showPassword);
    }

    const handleClickShowNewPassword = () => {
        setShowNewPassword(!showNewPassword);
    }

    const onDrop = (pictureFiles) => {
        setPictures(pictureFiles);
        setErrorPictures(false);
    }

    const onSubmitImg = e => {
        e.preventDefault();

        const options = {
            indices: true,
            nullsAsUndefineds: true
        };
        const formData = objectToFormData({
            img: pictures[0],
            publicId: imagesList[0]
        }, options);


        changeImg(formData);
        setPictures([]);
    }
    const onSubmitPassword = e => {
        e.preventDefault();
        changePassword({
            currentPassword: password,
            newPassword: newPassword
        })
    }
    const onSubmitPersonalData = e => {
        e.preventDefault();
        editData({
            first_name: newFirstName,
            age: newAge,
            interests: newInterests,
            gender: newGander,
            genderpartner: newGanderPartner,
            aboutme: newAboutMe,
            max_distance: newMaxDistance,
            min_age: newMinAge,
            max_age: newMaxAge
        })
    }
    return (
        <Container className={classes.root}>
            <Grid container>
                <Grid item xl={8} lg={8} md={8} sm={12} xs={12}>
                    <Card className={classes.card}>
                        <CardContent>
                            <Typography variant={"h5"}>
                                Edit personal data
                            </Typography>
                            <form onSubmit={onSubmitPersonalData} noValidate={false} autoComplete="off">
                                <FormControl variant="outlined" className={classes.formControl} fullWidth={true}
                                             error={nameError}>
                                    <TextField
                                        label={"Enter new name"}
                                        id={"enter-name"}
                                        value={newFirstName}
                                        onChange={e => {
                                            setNewFirstName(e.target.value);
                                            setNameError(false)
                                        }}
                                        aria-describedby={"enter-name-helper"}
                                    />
                                    <FormHelperText
                                        id={"enter-name-helper"}>{nameError && "Name is require"}</FormHelperText>
                                </FormControl>
                                <FormControl className={classes.formControl} fullWidth={true}>
                                    <TextField
                                        label={"Enter new interest"}
                                        id="enter-interest"
                                        multiline
                                        value={newInterests}
                                        onChange={e => setNewInterests(e.target.value)}
                                    />
                                </FormControl>
                                <FormControl className={classes.formControl} fullWidth={true}>
                                    <TextField
                                        label={"Enter new about you"}
                                        id="enter-about-me"
                                        multiline
                                        value={newAboutMe}
                                        onChange={e => setNewAboutMe(e.target.value)}
                                    />
                                </FormControl>
                                <FormControl component="fieldset" className={classes.formControl} fullWidth={true}>
                                    <FormLabel component="legend">Gender</FormLabel>
                                    <RadioGroup aria-label="gender" name="gender1" value={newGander}
                                                onChange={e => setNewGander(e.target.value)}>
                                        <FormControlLabel value="female" control={<Radio/>} label="Female"/>
                                        <FormControlLabel value="male" control={<Radio/>} label="Male"/>
                                    </RadioGroup>
                                </FormControl>
                                <FormControl component="fieldset" className={classes.formControl} fullWidth={true}>
                                    <FormLabel component="legend">Gender your partner</FormLabel>
                                    <RadioGroup aria-label="gender" name="gender1" value={newGanderPartner}
                                                onChange={e => setNewGanderPartner(e.target.value)}>
                                        <FormControlLabel value="female" control={<Radio/>} label="Female"/>
                                        <FormControlLabel value="male" control={<Radio/>} label="Male"/>
                                    </RadioGroup>
                                </FormControl>
                                <FormControl className={classes.formControl} fullWidth={true}>
                                    <FormLabel component="legend">Your age</FormLabel>
                                    <Slider
                                        value={newAge}
                                        onChange={(e, a) => setNewAge(a)}
                                        aria-labelledby="continuous-slider"
                                        valueLabelDisplay="auto"
                                        min={18}
                                        max={100}
                                    />
                                </FormControl>
                                <FormControl className={classes.formControl} fullWidth={true}>
                                    <FormLabel component="legend">New max distance</FormLabel>
                                    <Slider
                                        value={newMaxDistance}
                                        onChange={(e, a) => setNewMaxDistance(a)}
                                        aria-labelledby="continuous-slider"
                                        valueLabelDisplay="auto"
                                        min={1}
                                        max={60}
                                    />
                                </FormControl>
                                <FormControl className={classes.formControl} fullWidth={true}>
                                    <FormLabel component="legend">New min max age your partner</FormLabel>
                                    <Slider
                                        value={[newMinAge, newMaxAge]}
                                        onChange={(e, a) => {
                                            setNewMaxAge(_.max(a));
                                            setNewMinAge(_.min(a))
                                        }}
                                        aria-labelledby="continuous-slider"
                                        valueLabelDisplay="auto"
                                        min={18}
                                        max={100}
                                    />
                                </FormControl>
                                <Button
                                    type={"submit"}
                                    fullWidth={true}
                                    variant="contained"
                                    color="primary"
                                >Edit Personal Data</Button>
                            </form>
                        </CardContent>
                    </Card>
                    <Card className={classes.card}>
                        <CardContent>
                            <Typography variant={"h5"}>
                                Change password
                            </Typography>
                            <form onSubmit={onSubmitPassword} noValidate={false} autoComplete="off">
                                <FormControl variant="outlined" className={classes.formControl} fullWidth={true}
                                             error={passwordError}>
                                    <InputLabel htmlFor={"enter-password"}>Enter current password</InputLabel>
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
                                        id={"enter-password-helper"}>{passwordError && "Current password is require"}</FormHelperText>
                                </FormControl>
                                <FormControl variant="outlined" className={classes.formControl} fullWidth={true}
                                             error={newPasswordError}>
                                    <InputLabel htmlFor={"enter-new-password"}>Enter new password</InputLabel>
                                    <Input
                                        id={"enter-new-password"}
                                        value={newPassword}
                                        onChange={e => {
                                            setNewPassword(e.target.value);
                                            setNewPasswordError(false)
                                        }}
                                        aria-describedby={"enter-password-helper"}
                                        type={showNewPassword ? 'text' : 'password'}
                                        endAdornment={
                                            <InputAdornment position="end">
                                                <IconButton
                                                    aria-label="toggle password visibility"
                                                    onClick={handleClickShowNewPassword}
                                                    onMouseDown={handleMouseDownPassword}
                                                >
                                                    {showNewPassword ? <Visibility/> : <VisibilityOff/>}
                                                </IconButton>
                                            </InputAdornment>
                                        }
                                    />
                                    <FormHelperText
                                        id={"enter-password-helper"}>{newPasswordError && "Current password is require"}</FormHelperText>
                                </FormControl>
                                <Button type={"submit"} fullWidth={true} variant="contained" color="primary">Change
                                    password</Button>
                            </form>
                        </CardContent>
                    </Card>
                </Grid>
                <Grid item xl={4} lg={4} md={4} sm={12} xs={12}>
                    <Card className={classes.card}>
                        <CardContent>
                            <Typography variant={"h5"}>
                                Change your photo
                            </Typography>
                            <form onSubmit={onSubmitImg} noValidate={false} autoComplete="off">
                                <FormControl className={classes.formControl} variant="outlined" fullWidth={true}
                                             error={errorPictures}>
                                    <ImageUploader
                                        withPreview={true}
                                        withIcon={true}
                                        buttonText='Choose avatar'
                                        fileSizeError={"File size is too big"}
                                        fileTypeError={"Is not supported file extension"}
                                        onChange={onDrop}
                                        imgExtension={['.jpg', '.png', '.gif']}
                                        maxFileSize={9242880}
                                        singleImage={true}
                                    />
                                    <FormHelperText
                                        id={"enter-name-helper"}>{errorPictures && "Avatar is require"}</FormHelperText>
                                </FormControl>
                                <Button type={"submit"} fullWidth={true} variant="contained" color="primary">Change
                                    photo</Button>
                            </form>
                        </CardContent>
                    </Card>
                </Grid>
            </Grid>
        </Container>
    );
};

const mapStateToProps = (state) => {
    return {User: state.User};
};


const mapDispatchToProps = (dispatch) => {
    return {
        editData: bindActionCreators(UserAction.editData, dispatch),
        changePassword: bindActionCreators(UserAction.changePassword, dispatch),
        changeImg: bindActionCreators(UserAction.changeImg, dispatch),
    };
};


export default connect(mapStateToProps, mapDispatchToProps)(PersonalCabinet);
