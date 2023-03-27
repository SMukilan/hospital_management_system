
window.onload = () =>
{

    let serverRequest = new XMLHttpRequest();
        
    serverRequest.open("POST", "http://localhost:8080/HosManSysServlet/CheckSession");

    serverRequest.onreadystatechange = () =>
    {

        let mainContainer = document.getElementById("main_container");
        let body = document.getElementById("body");

        if (serverRequest.readyState == 4 && serverRequest.status == 200)
        {

            let parsedJson = JSON.parse(serverRequest.responseText);
            let validOrNot = parsedJson.Message;

            if (validOrNot == "validSession")
            {
                createDashBoard(mainContainer, serverRequest, parsedJson, body);
            }
            else
            {

                body.style.background = "url(Assets/Hospital-Management-System.jpg)";
                body.style.backgroundRepeat = "no-repeat";
                body.style.backgroundSize = "cover";

                mainContainer.innerHTML = '<div id = "welcome"><h2>Welcome!</h2><h3>A smart way to manage your hospital</h3><div><button class="sign" id="signInButt">Sign in</button><button class="sign" id="signUpButt">Sign up</button></div></div>';
                let welcomeDiv = document.getElementById("welcome");
                let signInButt = document.getElementById("signInButt");
                let signUpButt = document.getElementById("signUpButt");
            
                signInButt.onclick = () => createSignIn(mainContainer, serverRequest, body, welcomeDiv);
                signUpButt.onclick = () => createSignUp(mainContainer, serverRequest, body, welcomeDiv);

            }

        }
        else if (serverRequest.readyState == 4 && serverRequest.status == 400)
        {
            body.style.background = "url(Assets/Hospital-Management-System.jpg)";
            body.style.backgroundRepeat = "no-repeat";
            body.style.backgroundSize = "cover";

            mainContainer.innerHTML = '<div id = "welcome"><h2>Welcome!</h2><h3>A smart way to manage your hospital</h3><div><button class="sign" id="signInButt">Sign in</button><button class="sign" id="signUpButt">Sign up</button></div></div>';
            let welcomeDiv = document.getElementById("welcome");
            let signInButt = document.getElementById("signInButt");
            let signUpButt = document.getElementById("signUpButt");
            
            signInButt.onclick = () => createSignIn(mainContainer, serverRequest, body, welcomeDiv);
            signUpButt.onclick = () => createSignUp(mainContainer, serverRequest, body, welcomeDiv);
        }

    }

    serverRequest.send();

}





// ---------- Create sign in page ----------





function createSignIn(mainContainer, serverRequest, body, welcomeDiv)
{

    welcomeDiv.remove();
    let signInForm = document.createElement("div");
    signInForm.setAttribute("id", "signInForm");

    // element one

    let eleOne = document.createElement("div");
    let userId = document.createElement("input");
    let userIdWarn = document.createElement("span");

    userId.setAttribute("placeholder", "Enter user id or phone number");
    userId.setAttribute("type", "text");
    eleOne.appendChild(userId);
    eleOne.appendChild(userIdWarn);
    signInForm.appendChild(eleOne);

    // element two

    let eleTwo = document.createElement("div");
    let password = document.createElement("input");
    let passWarn = document.createElement("span");
    let showPassword = document.createElement("button");

    password.setAttribute("placeholder", "Enter password");
    password.setAttribute("type", "password");
    showPassword.innerText = "Show password";

    showPassword.onclick = () =>
    {
        if (password.type == "password")
        {
            password.setAttribute("type", "text");
            showPassword.innerText = "Hide password";
        }
        else
        {
            password.setAttribute("type", "password");
            showPassword.innerText = "Show password";
        }
    }
    
    eleTwo.appendChild(password);
    eleTwo.appendChild(passWarn);
    eleTwo.appendChild(showPassword);
    signInForm.appendChild(eleTwo);

    // element three

    let eleThree = document.createElement("div");
    let doSignInButt = document.createElement("button");

    doSignInButt.innerText = "Sign in";
    doSignInButt.setAttribute("class", "sign");
    eleThree.appendChild(doSignInButt);
    
    let warning = document.createElement("span");
    warning.setAttribute("id", "warning");

    eleThree.appendChild(warning);
    signInForm.appendChild(eleThree);

    // element four

    let question = document.createElement("p");
    question.innerText = "Don't have any account? ";

    let link = document.createElement("a");
    link.setAttribute("id", "hLink");
    link.innerText = "Sign up.";

    question.appendChild(link);
    signInForm.appendChild(question);

    // element five

    let cancelButt = document.createElement("button");
    cancelButt.innerText = "Cancel";

    signInForm.appendChild(cancelButt);

    // actions

    doSignInButt.onclick = () => doSignin(mainContainer, userId, userIdWarn, password, passWarn,
         warning, serverRequest, body);
    link.onclick = () =>
    {
        signInForm.remove();
        createSignUp(mainContainer, serverRequest, body, welcomeDiv);
    };

    cancelButt.onclick = () =>
    {
        signInForm.remove()
        mainContainer.appendChild(welcomeDiv);
    };

    mainContainer.appendChild(signInForm);

}

// Do Sign in

function doSignin(mainContainer, userId, userIdWarn, password, passWarn, warning, serverRequest, body)
{
    
    let responseJson = {};
    let userIdValue = userId.value.trim();
    let passwordValue = password.value.trim();
    
    userIdWarn.innerText = "";
    passWarn.innerText = "";
    warning.innerText = "";
    
    userId.style.borderColor = "";
    password.style.borderColor = "";
    
    if (userIdValue.length == 0 && passwordValue.length == 0)
    {
        userIdWarn.innerText = "Please fill in this field";
        passWarn.innerText = "Please fill in this field";
    
        userId.style.borderColor = "red";
        password.style.borderColor = "red";
    }
    else if (userIdValue.length == 0)
    {
        userId.style.borderColor = "red";
        userIdWarn.innerText = "Please fill in this field";
    }
    else if (passwordValue.length == 0)
    {
        password.style.borderColor = "red";
        passWarn.innerText = "Please fill in this field";
    }
    else
    {
        responseJson.userId = userIdValue;
        responseJson.password = passwordValue;
        
        serverRequest.open("POST" ,"http://localhost:8080/HosManSysServlet/Signin");
        serverRequest.setRequestHeader("Content-Type" ,"application/json");
        serverRequest.send(JSON.stringify(responseJson));
    
        serverRequest.onreadystatechange = () =>
        {
            if (serverRequest.readyState == 4)
            {

                let parsedJson = JSON.parse(serverRequest.responseText);
                if (serverRequest.status == 400)
                {
                    warning.innerText = parsedJson.Message;
                    userId.style.borderColor = "red";
                    password.style.borderColor = "red";
                }
                else if (serverRequest.status == 200)
                {
                    alert(parsedJson.Message);
                    document.cookie = serverRequest.cookie;
                    createDashBoard(mainContainer, serverRequest, parsedJson, body);
                }
            }
        }
    }
    
}





// ---------- Create sign up page ----------





function createSignUp(mainContainer, serverRequest, body, welcomeDiv)
{

    welcomeDiv.remove();
    let signUpForm = document.createElement("div");
    signUpForm.setAttribute("id", "signupForm");

    // element one

    let eleOne = document.createElement("div");
    let name = document.createElement("input");
    let nameWarn = document.createElement("span");

    name.setAttribute("placeholder", "Enter name");
    name.setAttribute("type", "text");
    eleOne.appendChild(name);
    eleOne.appendChild(nameWarn);
    signUpForm.appendChild(eleOne);

    // element Two

    let eleTwo = document.createElement("div");
    let phoneNumber = document.createElement("input");
    let phNumWarn = document.createElement("span");

    phoneNumber.setAttribute("placeholder", "Enter phone number");
    phoneNumber.setAttribute("type", "number");
    eleTwo.appendChild(phoneNumber);
    eleTwo.appendChild(phNumWarn);
    signUpForm.appendChild(eleTwo);

    // element Three

    let eleThree = document.createElement("div");
    let password = document.createElement("input");
    let passWarn = document.createElement("span");

    password.setAttribute("placeholder", "Create password");
    password.setAttribute("type", "password");
    eleThree.appendChild(password);
    eleThree.appendChild(passWarn);
    signUpForm.appendChild(eleThree);
    
    // element Four

    let eleFour = document.createElement("div");
    let confirmPassword = document.createElement("input");
    let confirmPassWarn = document.createElement("span");
    let showPassword = document.createElement("button");

    confirmPassword.setAttribute("placeholder", "Confirm password");
    confirmPassword.setAttribute("type", "password");
    showPassword.innerText = "Show password";

    showPassword.onclick = () =>
    {
        if (password.type == "password")
        {
            password.setAttribute("type", "text");
            confirmPassword.setAttribute("type", "text");
            showPassword.innerText = "Hide password";
        }
        else
        {
            password.setAttribute("type", "password");
            confirmPassword.setAttribute("type", "password");
            showPassword.innerText = "Show password";
        }
    }

    eleFour.appendChild(confirmPassword);
    eleFour.appendChild(confirmPassWarn);
    eleFour.appendChild(showPassword);
    signUpForm.appendChild(eleFour);

    // element five

    let eleFive = document.createElement("div");
    let hospitalName = document.createElement("input");
    let hospitalNameWarn = document.createElement("span");

    hospitalName.setAttribute("placeholder", "Enter hospital name");
    hospitalName.setAttribute("type", "text");
    eleFive.appendChild(hospitalName);
    eleFive.appendChild(hospitalNameWarn);
    signUpForm.appendChild(eleFive);

    // element six

    let eleSix = document.createElement("div");
    let doSignUpButt = document.createElement("button");

    doSignUpButt.innerText = "Sign up";
    doSignUpButt.setAttribute("class", "sign");
    eleSix.appendChild(doSignUpButt);
    
    let warning = document.createElement("span");
    warning.setAttribute("id", "warning");

    eleSix.appendChild(warning);
    signUpForm.appendChild(eleSix);

    // element eight

    let question = document.createElement("p");
    question.innerText = "Already have an account? ";

    let link = document.createElement("a");
    link.setAttribute("id", "hLink");
    link.innerText = "Sign in.";

    question.appendChild(link);
    signUpForm.appendChild(question);

    // element nine

    let cancelButt = document.createElement("button");
    cancelButt.innerText = "Cancel";

    signUpForm.appendChild(cancelButt);

    // actions

    doSignUpButt.onclick = () => doSignup(mainContainer, name, nameWarn, phoneNumber, phNumWarn, 
        password, passWarn, confirmPassword, confirmPassWarn, hospitalName,
         hospitalNameWarn, warning, serverRequest, body);

    link.onclick = () =>
    {
        signUpForm.remove();
        createSignIn(mainContainer, serverRequest, body, welcomeDiv);
    }
    cancelButt.onclick = () =>
    {
        signUpForm.remove();
        mainContainer.appendChild(welcomeDiv);
    };

    mainContainer.appendChild(signUpForm);

}

// Do sign in

function doSignup(mainContainer, name, nameWarn, phoneNumber, phNumWarn, 
    password, passWarn, confirmPassword, confirmPassWarn, hospitalName,
     hospitalNameWarn, warning, serverRequest, body)
{
    
    let responseJson = {};
    let nameValue = name.value.trim();
    let phoneNumberValue = phoneNumber.value.trim().replaceAll(" ", "");
    let passwordValue = password.value.trim();
    let confirmPasswordValue = confirmPassword.value.trim();
    let hospitalNameValue = hospitalName.value.trim();

    nameWarn.innerText = "";
    phNumWarn.innerText = "";
    passWarn.innerText = "";
    confirmPassWarn.innerText = "";
    hospitalNameWarn.innerText = "";
    warning.innerText = "";

    name.style.borderColor = "";
    phoneNumber.style.borderColor = "";
    password.style.borderColor = "";
    confirmPassword.style.borderColor = "";
    hospitalName.style.borderColor = "";

    if (!/^[a-zA-Z.\s]+$/.test(nameValue) || nameValue.length < 2 || nameValue.length > 80)
    {
        nameWarn.innerText = "Enter a valid name. Don't use numbers and special characters!";
        name.style.borderColor = "red";
    }
    else if (!/^\d+$/.test(phoneNumberValue) || phoneNumberValue.length != 10)
    {
        phNumWarn.innerText = "Enter a valid phone number.";
        phoneNumber.style.borderColor = "red";
    }
    else if (!/^(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{6,16}$/.test(passwordValue))
    {
        passWarn.innerText = "Use a strong password";
        password.style.borderColor = "red";
    }
    else if (passwordValue != confirmPasswordValue)
    {
        passWarn.innerText = "Password doesn't match.!";
        confirmPassWarn.innerText = "Password doesn't match.!";

        password.style.borderColor = "red";
        confirmPassword.style.borderColor = "red";
    }
    else if (!/^[a-zA-Z.\s]+$/.test(hospitalNameValue))
    {
        hospitalNameWarn.innerText = "Enter a valid hospital name. Don't use numbers and special characters!";
        hospitalName.style.borderColor = "red";
    }
    else
    {

        responseJson.name = nameValue;
        responseJson.phoneNumber = phoneNumberValue;
        responseJson.password = passwordValue;
        responseJson.confirmPassword = confirmPasswordValue;
        responseJson.hospitalName = hospitalNameValue;

        serverRequest.open("POST" ,"http://localhost:8080/HosManSysServlet/Signup");
        serverRequest.setRequestHeader("Content-Type" ,"application/json");
        serverRequest.send(JSON.stringify(responseJson));

        serverRequest.onreadystatechange = () =>
        {

            if (serverRequest.readyState == 4)
            {

                let parsedJson = JSON.parse(serverRequest.responseText);

                if (serverRequest.status == 400)
                {

                    if (parsedJson.name.length != 0)
                    {
                        nameWarn.innerText = parsedJson.name;
                        name.style.borderColor = "red";
                    }
                    if (parsedJson.phoneNumber.length != 0)
                    {
                        phNumWarn.innerText = parsedJson.phoneNumber;
                        phoneNumber.style.borderColor = "red";
                    }
                    if (parsedJson.password.length != 0)
                    {
                        passWarn.innerText = parsedJson.password;
                        password.style.borderColor = "red";
                    }
                    if (parsedJson.confirmPassword.length != 0)
                    {
                        confirmPassWarn.innerText = parsedJson.confirmPassword;
                        confirmPassword.style.borderColor = "red";
                    }
                    if (parsedJson.hospitalName.length != 0)
                    {
                        hospitalNameWarn.innerText = parsedJson.hospitalName;
                        hospitalName.style.borderColor = "red";
                    }
                    if (parsedJson.hospitalName.length != 0)
                    {
                        warning.innerText = parsedJson.Message;
                    }

                }
                else if (serverRequest.status == 200)
                {
                    alert(parsedJson.Message);
                    document.cookie = serverRequest.cookie;
                    createDashBoard(mainContainer, serverRequest, parsedJson, body);
                }
            }
        }

    }

}





// ---------- Dashboard Management ----------





function createDashBoard(mainContainer, serverRequest, userDetails, body)
{

    let popupBack = document.createElement("div");
    popupBack.classList.add("popupBack");
    popupBack.id = "popupBack";
    document.addEventListener("keydown", function(event)
    {
        if (event.key === "Escape")
        {
            popupBack.innerHTML = "";
            popupBack.remove();
        }
    });

    let header = document.getElementById("header");
    
    let profileContain = document.createElement("div");
    profileContain.id = "profileContain";
    profileContain.innerHTML = '<lord-icon src="https://cdn.lordicon.com/ajkxzzfb.json" trigger="hover" style="width:60px;height:60px"></lord-icon>';

    header.appendChild(profileContain);
    
    body.style.background = "#62deff";
    mainContainer.innerHTML = "";
    mainContainer.id = "dashboard";

    let nameDetails = document.createElement("div");
    nameDetails.style.width = "150px";
        let userName = document.createElement("h3");
        userName.innerText = userDetails.userName;
        userName.style.margin = "5px";

        let userId = document.createElement("h5");
        userId.innerText = "User Id: " + ((userDetails.userId.length > 10)? userDetails.userId.substring(0, 15) + "...": userDetails.userId);
        userId.style.margin = "5px";

    nameDetails.appendChild(userName);
    nameDetails.appendChild(userId);
    profileContain.appendChild(nameDetails);

    let userOptions = document.createElement("div");
    userOptions.id = "userOptions";

    let profile = document.createElement("h3");
    profile.innerText = "View profile";

    let line = document.createElement("hr");

    let logOut = document.createElement("h3");
    logOut.innerText = "Log out";

    userOptions.appendChild(profile);
    userOptions.appendChild(line);
    // userOptions.innerHTML +="<hr>";
    userOptions.appendChild(logOut);

    // Doctor manage section creation

    let docManageSection = document.createElement("div");
    docManageSection.classList.add("manageSection");

    // Doc manage sec heading creation

    let docHeading = document.createElement("h2");
    docHeading.innerText = "Doctors details manage";

    // Other action section creation
    
    let docOtherActions = document.createElement("div");
    docOtherActions.classList.add("otherActions");

        // Add doctor button creation

        let addDoc = document.createElement("button");
        addDoc.innerHTML = '<lord-icon src="https://cdn.lordicon.com/mecwbjnp.json"trigger="loop-on-hover"delay="2000"colors="primary:#121331,secondary:#01c4f8"style="width:24px;height:24px;margin-bottom:-5px" stroke="100"></lord-icon> Add doctor';
        addDoc.classList.add("actionButt");

        let totalDocs = document.createElement("h3");
        totalDocs.id = "totalDocs";
        totalDocs.style.marginLeft = "100px";

        docOtherActions.appendChild(addDoc);
        docOtherActions.appendChild(totalDocs);

    // Doctors list section creation

    let docListSection = document.createElement("div");
    docListSection.classList.add("listSection");

        // Options for list section creation

        let docListSecOptions = document.createElement("div");
        docListSecOptions.classList.add("actionSection");

            // Search box creation

            let docSearch = document.createElement("input");
            docSearch.setAttribute("type", "search");
            docSearch.setAttribute("placeholder", "Search on doctors");

            // Filter section creation

            let docFilterSec = document.createElement("div");

                // Filter label creation

                let docFilLabel = document.createElement("label");
                docFilLabel.innerText = "Filter: ";

                // Filter select element creation

                let docFilter = document.createElement("select");

                    // Filter section options creation

                    let filOptionZero = document.createElement("option");
                    filOptionZero.setAttribute("value", "active");
                    filOptionZero.innerText = "Active";

                    let filOptionOne = document.createElement("option");
                    filOptionOne.setAttribute("value", "removed");
                    filOptionOne.innerText = "Removed";

                    let filOptionTwo = document.createElement("option");
                    filOptionTwo.setAttribute("value", "all");
                    filOptionTwo.innerText = "All";

                    docFilter.appendChild(filOptionZero);
                    docFilter.appendChild(filOptionOne);
                    docFilter.appendChild(filOptionTwo);
            
                docFilterSec.appendChild(docFilLabel);
                docFilterSec.appendChild(docFilter);
                
            docListSecOptions.appendChild(docSearch);
            docListSecOptions.appendChild(docFilterSec);

            // Sort by section creation

            let docSortBySec = document.createElement("div");

                // Sort by label creation

                let docSortByLabel = document.createElement("label");
                docSortByLabel.innerText = "Sort by: ";

                // Sort by select element creation

                let docSortby = document.createElement("select");

                    // Sort by section options creation

                    let sortByOpZero = document.createElement("option");
                    sortByOpZero.setAttribute("value", "a-z");
                    sortByOpZero.innerText = "A - Z";

                    let sortByOpOne = document.createElement("option");
                    sortByOpOne.setAttribute("value", "z-a");
                    sortByOpOne.innerText = "Z - A";

                    docSortby.appendChild(sortByOpZero);
                    docSortby.appendChild(sortByOpOne);

                docSortBySec.appendChild(docSortByLabel);
                docSortBySec.appendChild(docSortby);

            docListSecOptions.appendChild(docSortBySec);

        // Doctors list creation 

        let docsList = document.createElement("div");
        docsList.classList.add("list");

        docListSection.appendChild(docListSecOptions);
        docListSection.appendChild(docsList);

    docManageSection.appendChild(docHeading);
    docManageSection.appendChild(docOtherActions);
    docManageSection.appendChild(docListSection);

    // Patient manage section creation

    let patientManageSection = document.createElement("div");
    patientManageSection.classList.add("manageSection");

    // Patient manage section heading creation

    let patientHeading = document.createElement("h2");
    patientHeading.innerText = "Patients details manage";

    // Other action section creation

    let patientOtherActions = document.createElement("div");
    patientOtherActions.classList.add("otherActions");

        // Creating admit patient button

        let admitPati = document.createElement("button");
        admitPati.innerHTML = '<lord-icon src="https://cdn.lordicon.com/mecwbjnp.json"trigger="loop-on-hover"delay="2000"colors="primary:#121331,secondary:#01c4f8"style="width:24px;height:24px;margin-bottom:-5px" stroke="100"></lord-icon> Admit patient';
        admitPati.classList.add("actionButt");

        let totalPatients = document.createElement("h3");
        totalPatients.id = "totalPatients";
        totalPatients.style.marginLeft = "100px";

        patientOtherActions.appendChild(admitPati);
        patientOtherActions.appendChild(totalPatients);

    // Patients list section creation

    let patientListSection = document.createElement("div");
    patientListSection.classList.add("listSection");

        // Options for list section creation

        let patiListSecOptions = document.createElement("div");
        patiListSecOptions.classList.add("actionSection");

            // Search box creation

            let patiSearch = document.createElement("input");
            patiSearch.setAttribute("type", "search");
            patiSearch.setAttribute("placeholder", "Search on patients");

            // Filter section creation

            let patiFilterSec = document.createElement("div");

                // Filter label creation

                let patiFilLabel = document.createElement("label");
                patiFilLabel.innerText = "Filter: ";

                // Filter select element creation

                let patiFilter = document.createElement("select");

                    // Filter section options creation

                    let patiFilOptionZero = document.createElement("option");
                    patiFilOptionZero.setAttribute("value", "admitted");
                    patiFilOptionZero.innerText = "Admitted";

                    let patiFilOptionOne = document.createElement("option");
                    patiFilOptionOne.setAttribute("value", "discharged");
                    patiFilOptionOne.innerText = "Discharged";

                    let patiFilOptionTwo = document.createElement("option");
                    patiFilOptionTwo.setAttribute("value", "all");
                    patiFilOptionTwo.innerText = "All";

                    patiFilter.appendChild(patiFilOptionZero);
                    patiFilter.appendChild(patiFilOptionOne);
                    patiFilter.appendChild(patiFilOptionTwo);
            
                patiFilterSec.appendChild(patiFilLabel);
                patiFilterSec.appendChild(patiFilter);
                
            patiListSecOptions.appendChild(patiSearch);
            patiListSecOptions.appendChild(patiFilterSec);

            // Sort by section creation

            let patiSortBySec = document.createElement("div");

                // Sort by label creation

                let patiSortByLabel = document.createElement("label");
                patiSortByLabel.innerText = "Sort by: ";

                // Sort by select element creation

                let patiSortby = document.createElement("select");

                    // Sort by section options creation

                    let patiSortByOpZero = document.createElement("option");
                    patiSortByOpZero.setAttribute("value", "a-z");
                    patiSortByOpZero.innerText = "A - Z";

                    let patiSortByOpOne = document.createElement("option");
                    patiSortByOpOne.setAttribute("value", "z-a");
                    patiSortByOpOne.innerText = "Z - A";

                    let patiSortByOpTwo = document.createElement("option");
                    patiSortByOpTwo.setAttribute("value", "firstAdmitted");
                    patiSortByOpTwo.innerText = "First admitted";

                    let patiSortByOpThree = document.createElement("option");
                    patiSortByOpThree.setAttribute("value", "lastAdmitted");
                    patiSortByOpThree.innerText = "Last admitted";

                    patiSortby.appendChild(patiSortByOpTwo);
                    patiSortby.appendChild(patiSortByOpThree);
                    patiSortby.appendChild(patiSortByOpZero);
                    patiSortby.appendChild(patiSortByOpOne);

                patiSortBySec.appendChild(patiSortByLabel);
                patiSortBySec.appendChild(patiSortby);

            patiListSecOptions.appendChild(patiSortBySec);

        // Patients list creation 

        let patientsList = document.createElement("div");
        patientsList.classList.add("list");

        patientListSection.appendChild(patiListSecOptions);
        patientListSection.appendChild(patientsList);

    patientManageSection.appendChild(patientHeading);
    patientManageSection.appendChild(patientOtherActions);
    patientManageSection.appendChild(patientListSection);

    mainContainer.appendChild(docManageSection);
    mainContainer.appendChild(patientManageSection);

    profileContain.onclick = () =>
    {
        header.appendChild(userOptions);
    };

    body.onclick = (event) =>
    {
        if (event.target.id == "body" || event.target.id == "header")
            userOptions.remove();
    };

    logOut.onclick = () =>
    {

        serverRequest.open("POST" ,"http://localhost:8080/HosManSysServlet/LogOut");
        serverRequest.send();

        serverRequest.onreadystatechange = () =>
        {

            if (serverRequest.readyState == 4 && serverRequest.status == 200)
            {
                location.reload();
            }

        }

    };
    profile.onclick = () => viewProflie(serverRequest, userDetails, userOptions, popupBack, body);

    addDoc.onclick = () => addDoctor(docSearch, serverRequest, docFilter, docSortby, docsList, popupBack, body);
    admitPati.onclick = () => admitPatient(patiSearch, serverRequest, patiFilter, patiSortby, patientsList, popupBack, body);

    getDoctors(docSearch, serverRequest, docFilter, docSortby, docsList, popupBack, body);
    docSearch.oninput = () => getDoctors(docSearch, serverRequest, docFilter, docSortby, docsList, popupBack, body);
    docFilter.onchange = () => getDoctors(docSearch, serverRequest, docFilter, docSortby, docsList, popupBack, body);
    docSortby.onchange = () => getDoctors(docSearch, serverRequest, docFilter, docSortby, docsList, popupBack, body);

    getPatients(patiSearch, patiFilter, patiSortby, patientsList, popupBack, body);
    patiSearch.oninput = () => getPatients(patiSearch, patiFilter, patiSortby, patientsList, popupBack, body);
    patiFilter.onchange = () => getPatients(patiSearch, patiFilter, patiSortby, patientsList, popupBack, body);
    patiSortby.onchange = () => getPatients(patiSearch, patiFilter, patiSortby, patientsList, popupBack, body);

}

function viewProflie(serverRequest, userDetails, userOptions, popupBack, body)
{

    userOptions.remove();
    console.log("hii");
    popupBack.innerHTML = "";

    let detailsBox = document.createElement("div");
    detailsBox.classList.add("addDoc");
    detailsBox.style.width = "400px";
    detailsBox.style.padding = "50px 100px";
    detailsBox.style.alignItems = "start";

    let title = document.createElement("h2");
    title.innerText = "Profile";
    title.style.margin = "20px auto";

    let name = document.createElement("h3");
    name.innerText = "Name: " + userDetails.userName;

    let id = document.createElement("h3");
    id.innerText = "User Id: " + userDetails.userId;

    let password = document.createElement("h3");
    password.innerText = "Phone number: " + userDetails.password;

    let phNumber = document.createElement("h3");
    phNumber.innerText = "Phone number: " + userDetails.phoneNumber;

    let hopitalName = document.createElement("h3");
    hopitalName.innerText = "Hopital name: " + userDetails.hopitalName;

    let options = document.createElement("div");
        options.style.width = "300px";
        options.style.justifyContent = "space-between";
        options.style.flexDirection = "row";
        options.style.margin = "20px auto";
    let editDetails = document.createElement("button");
    editDetails.innerText = "Edit details";

    let close = document.createElement("button");
    close.innerText = "Close";

    options.appendChild(editDetails);
    options.appendChild(close);

    detailsBox.appendChild(title);
    detailsBox.appendChild(name);
    detailsBox.appendChild(id);
    detailsBox.appendChild(password);
    detailsBox.appendChild(phNumber);
    detailsBox.appendChild(hopitalName);
    detailsBox.appendChild(options);

    close.onclick = () => popupBack.remove();

    popupBack.appendChild(detailsBox);
    body.appendChild(popupBack);

}

// ---------- Doctor section ----------

function getDoctors(docSearch, serverRequest, filter, sortby, list, popupBack, body)
{

    let responseJson = {};
    responseJson.searchKey = (docSearch.value == undefined)? "": docSearch.value.trim();
    responseJson.filter = filter.value;
    responseJson.sortby = sortby.value;

    serverRequest.open("POST" ,"http://localhost:8080/HosManSysServlet/GetDoctors");
    serverRequest.setRequestHeader("Content-Type" ,"application/json");
    serverRequest.send(JSON.stringify(responseJson));

    serverRequest.onreadystatechange = () =>
    {

        if (serverRequest.readyState == 4)
        {

            list.innerHTML = "";

            if (serverRequest.status == 200)
            {

                let parsedJson = JSON.parse(serverRequest.responseText.substring(0, serverRequest.responseText.indexOf("]") + 1));
                document.getElementById("totalDocs").innerText = "Number of active doctors: " 
                    + serverRequest.responseText.substring(serverRequest.responseText.indexOf("]") + 1, serverRequest.responseText.length);
                if (parsedJson.length == 0)
                {
                    list.innerHTML = "<h2>No doctors available!!</h2>";
                }
                else
                {

                    for (let i = 0; i < parsedJson.length; i ++)
                    {

                        let docDetails = document.createElement("div");
                        docDetails.classList.add("details");

                        let docIcon = document.createElement("img");
                        docIcon.classList.add("listIcon");
                        docIcon.setAttribute("src", "Assets/doc.svg");

                        let nameDetails = document.createElement("div");
                        nameDetails.style.width = "210px";
                            let docName = document.createElement("h3");
                            docName.innerText = parsedJson[i].doctorName;
                            docName.style.margin = "5px";

                            let docId = document.createElement("h5");
                            docId.innerText = "Doctor Id: " + ((parsedJson[i].docId.length > 10)? parsedJson[i].docId.substring(0, 15) + "...": parsedJson[i].docId);
                            docId.style.margin = "5px";

                            nameDetails.appendChild(docName);
                            nameDetails.appendChild(docId);

                        let otherDetails = document.createElement("div");
                        otherDetails.style.marginLeft = "10px";
                        otherDetails.style.width = "210px";
                            let phNumber = document.createElement("h4");
                            phNumber.innerText = "Ph no: " + parsedJson[i].phNumber;
                            phNumber.style.margin = "5px";

                            let timing = document.createElement("h5");
                            timing.innerText = "Timing: " + parsedJson[i].timing;
                            timing.style.margin = "5px";

                            otherDetails.appendChild(phNumber);
                            otherDetails.appendChild(timing);

                        docDetails.appendChild(docIcon);
                        docDetails.appendChild(nameDetails);
                        docDetails.appendChild(otherDetails);

                        list.appendChild(docDetails);

                        let removeButt = document.createElement("button");
                        removeButt.classList.add("removeButt");
                        removeButt.id = "removeButt";
                        removeButt.innerText = "Remove";

                        if (parsedJson[i].doctorStatus == "AVAILABLE")
                        {

                            let status = document.createElement("h3");
                            status.innerText = "Active";
                            status.style.marginLeft = "55px";

                            docDetails.appendChild(status);
                            docDetails.onmouseover = () =>
                            {
                                status.remove();
                                docDetails.appendChild(removeButt);
                            };
    
                            docDetails.onmouseleave = () =>
                            {
                                removeButt.remove();
                                docDetails.appendChild(status);
                            }

                        }
                        else
                        {
                            let status = document.createElement("h3");
                            status.innerText = "Removed";
                            status.style.marginLeft = "55px";

                            docDetails.appendChild(status);
                        }

                        docDetails.onclick = (event) => viewDocDetails(event, parsedJson[i], docSearch, serverRequest, filter, sortby, list, popupBack, body);
                        removeButt.onclick = () => removeDoctor(parsedJson[i], docSearch, serverRequest, filter, sortby, list, popupBack, body);

                    }

                }

            }
            else if (serverRequest.status == 400)
            {
                alert(JSON.parse(serverRequest.responseText).Message);
            }

        }
        
    }

}

function removeDoctor(docDetails, docSearch, serverRequest, filter, sortby, list, popupBack, body)
{

    popupBack.innerHTML = "";
    let conformationBox = document.createElement("div");
    conformationBox.classList.add("confirmBox");
        let confirmId = document.createElement("h2");
        confirmId.innerText = "Doctor id: " + docDetails.docId;

        let confirmText = document.createElement("h2");
        confirmText.style.margin = "0px 0px 20px 0px";
        confirmText.innerText = "Are you sure to remove the doctor?";

        let optionDiv = document.createElement("div");
            let yesButt = document.createElement("button");
            yesButt.innerText = "Yes";

            let cancelButt = document.createElement("button");
            cancelButt.innerText = "Cancel";

            optionDiv.appendChild(yesButt);
            optionDiv.appendChild(cancelButt);

        conformationBox.appendChild(confirmId);
        conformationBox.appendChild(confirmText);
        conformationBox.appendChild(optionDiv);

    popupBack.appendChild(conformationBox);

    cancelButt.onclick = () => popupBack.remove();
    yesButt.onclick = () =>
    {
        let responseJson = {};
        responseJson.docId = docDetails.docId;

        serverRequest.open("POST" ,"http://localhost:8080/HosManSysServlet/RemoveDoctor");
        serverRequest.setRequestHeader("Content-Type" ,"application/json");
        serverRequest.send(JSON.stringify(responseJson));

        serverRequest.onreadystatechange = () =>
        {
            
            if (serverRequest.readyState == 4)
            {

                popupBack.remove();
                let message = JSON.parse(serverRequest.responseText);
                if (serverRequest.status == 400)
                {
                    alert(message.Message);
                }
                else if (serverRequest.status == 200)
                {
                    alert(message.Message);
                    getDoctors(docSearch, serverRequest, filter, sortby, list, popupBack, body);
                }

            }

        };

    };
    body.appendChild(popupBack);

}

function addDoctor(docSearch, serverRequest, filter, sortby, list, popupBack, body)
{

    popupBack.innerHTML = "";
    let form = document.createElement("div");

        let title = document.createElement("h2");
        title.innerText = "Add doctor";

        // input element one 

        let elementOne = document.createElement("div"); 
        let docName = document.createElement("input");
        let docnameWarn = document.createElement("span");

        docName.setAttribute("type", "text");
        docName.setAttribute("placeholder", "Doctor name");
        elementOne.appendChild(docName);
        elementOne.appendChild(docnameWarn);

        // input element two

        let elementTwo = document.createElement("div");
        let phoneNumber = document.createElement("input");
        let phNumWarn = document.createElement("span");

        phoneNumber.setAttribute("type", "number");
        phoneNumber.setAttribute("placeholder", "Phone number");
        elementTwo.appendChild(phoneNumber);
        elementTwo.appendChild(phNumWarn);

        // input element three

        let elementThree = document.createElement("div");
        let specialist = document.createElement("input");
        let specialistWarn = document.createElement("span");

        specialist.setAttribute("type", "text");
        specialist.setAttribute("placeholder", "Specialist type");
        elementThree.appendChild(specialist);
        elementThree.appendChild(specialistWarn);

        // input element four

        let elementFour = document.createElement("div");
        let qualification = document.createElement("input");
        let qualiWarn = document.createElement("span");

        qualification.setAttribute("type", "text");
        qualification.setAttribute("placeholder", "Qualification");
        elementFour.appendChild(qualification);
        elementFour.appendChild(qualiWarn);

        // input element five

        let elementFive = document.createElement("div");
        let elementFiveInner = document.createElement("div");
            let startAvailableTime = document.createElement("input");
            let amOrPm = document.createElement("select");

            amOrPm.innerHTML = '<option value = "AM">AM</option><option value = "PM">PM</option>';
            startAvailableTime.setAttribute("type", "number");
            startAvailableTime.setAttribute("placeholder", "Starting available time(1 - 12)");
            startAvailableTime.style.marginLeft = "60px";
            amOrPm.style.marginLeft = "10px";
            elementFiveInner.appendChild(startAvailableTime);
            elementFiveInner.appendChild(amOrPm);

        let startAvailableTimeWarn = document.createElement("span");

        elementFive.appendChild(elementFiveInner);
        elementFive.appendChild(startAvailableTimeWarn);

        // input element six

        let elementSix = document.createElement("div");
        let totalAvailTime = document.createElement("input");
        let totalAvailTimeWarn = document.createElement("span");

        totalAvailTime.setAttribute("type", "number");
        totalAvailTime.setAttribute("placeholder", "Total available time(5 - 10)");
        elementSix.appendChild(totalAvailTime);
        elementSix.appendChild(totalAvailTimeWarn);

        // action section

        let actionSection = document.createElement("div");
        let submit = document.createElement("button");
        let cancel = document.createElement("button");

        actionSection.style.flexDirection = "row";
        actionSection.style.width = "300px";
        actionSection.style.justifyContent = "space-between";
        submit.innerText = "Add";
        cancel.innerText = "Cancel";
        actionSection.appendChild(submit);
        actionSection.appendChild(cancel);

    form.appendChild(title);
    form.appendChild(elementOne);
    form.appendChild(elementTwo);
    form.appendChild(elementThree);
    form.appendChild(elementFour);
    form.appendChild(elementFive);
    form.appendChild(elementSix);
    form.appendChild(actionSection);

    form.classList.add("addDoc");
    popupBack.appendChild(form);
    body.appendChild(popupBack);

    submit.onclick = () => validateAddDoctor(serverRequest, docName, phoneNumber, specialist, qualification, startAvailableTime, amOrPm,
        totalAvailTime, docnameWarn, phNumWarn, specialistWarn, qualiWarn, startAvailableTimeWarn, totalAvailTimeWarn,
        docSearch, filter, sortby, list, popupBack, body);
    cancel.onclick = () => popupBack.remove();

}

function validateAddDoctor(serverRequest, docName, phoneNumber, specialist, qualification, startAvailableTime, amOrPm,
    totalAvailTime, docnameWarn, phNumWarn, specialistWarn, qualiWarn, startAvailableTimeWarn, totalAvailTimeWarn,
    docSearch, filter, sortby, list, popupBack, body)
{

    docnameWarn.innerText = "";
    phNumWarn.innerText = "";
    specialistWarn.innerText = "";
    qualiWarn.innerText = "";
    startAvailableTimeWarn.innerText = "";
    totalAvailTimeWarn.innerText = "";

    docName.style.borderColor = "";
    phoneNumber.style.borderColor = "";
    specialist.style.borderColor = "";
    qualification.style.borderColor = "";
    startAvailableTime.style.borderColor = "";
    amOrPm.style.borderColor = "";
    totalAvailTime.style.borderColor = "";

    let nameValue = docName.value.trim();
    let phoneNumberValue = phoneNumber.value.trim().replaceAll(" ", "");
    let specialistValue = specialist.value.trim();
    let qualificationValue = qualification.value.trim();
    let startAvailableTimeValue = startAvailableTime.value.trim();
    let amOrPmValue = amOrPm.value.trim();
    let totalAvailTimeValue = totalAvailTime.value.trim();

    if (!/^[a-zA-Z.\s]+$/.test(nameValue) || nameValue.length < 2 || nameValue.length > 80)
    {
        docnameWarn.innerText = "Enter a valid name. Don't use numbers and special characters!";
        docName.style.borderColor = "red";
    }
    else if (!/^\d+$/.test(phoneNumberValue) || phoneNumberValue.length != 10)
    {
        phNumWarn.innerText = "Enter a valid phone number.";
        phoneNumber.style.borderColor = "red";
    }
    else if (!/^[a-zA-Z.\s]+$/.test(specialistValue) || specialistValue.length < 2 || specialistValue.length > 80)
    {
        specialistWarn.innerText = "Enter a valid input. Don't use numbers and special characters!";
        specialist.style.borderColor = "red";
    }
    else if (!/^[a-zA-Z.\s]+$/.test(qualificationValue) || qualificationValue.length < 2 || qualificationValue.length > 80)
    {
        qualiWarn.innerText = "Enter a valid input. Don't use numbers and special characters!";
        qualification.style.borderColor = "red";
    }
    else if (!/^\d+$/.test(startAvailableTimeValue) || startAvailableTimeValue < 1 || startAvailableTimeValue > 12)
    {
        startAvailableTimeWarn.innerText = "Enter a valid input between 1 - 12. Enter numbers only";
        startAvailableTime.style.borderColor = "red";
    }
    else if (!(amOrPmValue == "AM" || amOrPmValue == "PM"))
    {
        startAvailableTimeWarn.innerText = "Enter only AM or PM";
        amOrPm.style.borderColor = "red";
    }
    else if (!/^\d+$/.test(totalAvailTimeValue) || totalAvailTimeValue < 5 || totalAvailTimeValue > 10)
    {
        totalAvailTimeWarn.innerText = "Enter a valid input between 5 - 10. Enter numbers only";
        totalAvailTime.style.borderColor = "red";
    }
    else
    {

        let responseJson = {};
        responseJson.name = nameValue;
        responseJson.phoneNumber = phoneNumberValue;
        responseJson.specialist = specialistValue;
        responseJson.qualification = qualificationValue;
        responseJson.startAvailableTime = startAvailableTimeValue;
        responseJson.amOrPm = amOrPmValue;
        responseJson.totalAvailTime = totalAvailTimeValue;

        serverRequest.open("POST" ,"http://localhost:8080/HosManSysServlet/AddDoctor");
        serverRequest.setRequestHeader("Content-Type" ,"application/json");
        serverRequest.send(JSON.stringify(responseJson));

        serverRequest.onreadystatechange = () =>
        {

            if (serverRequest.readyState == 4)
            {
                
                let parsedJson = JSON.parse(serverRequest.responseText);
                if (serverRequest.status == 400)
                {

                    if (parsedJson.name != undefined && parsedJson.name.length != 0)
                    {
                        docnameWarn.innerText = parsedJson.name;
                        docName.style.borderColor = "red";
                    }
                    if (parsedJson.phoneNumber != undefined && parsedJson.phoneNumber.length != 0)
                    {
                        phNumWarn.innerText = parsedJson.phoneNumber;
                        phoneNumber.style.borderColor = "red";
                    }
                    if (parsedJson.specialist != undefined && parsedJson.specialist.length != 0)
                    {
                        specialistWarn.innerText = parsedJson.specialist;
                        specialist.style.borderColor = "red";
                    }
                    if (parsedJson.qualification != undefined && parsedJson.qualification.length != 0)
                    {
                        qualiWarn.innerText = parsedJson.qualification;
                        qualification.style.borderColor = "red";
                    }
                    if (parsedJson.startAvailableTime != undefined && parsedJson.startAvailableTime.length != 0)
                    {
                        startAvailableTimeWarn.innerText = parsedJson.startAvailableTime;
                        startAvailableTime.style.borderColor = "red";
                    }
                    if (parsedJson.totalAvailTime != undefined && parsedJson.totalAvailTime.length != 0)
                    {
                        totalAvailTimeWarn.innerText = parsedJson.totalAvailTime;
                        totalAvailTime.style.borderColor = "red";
                    }
                    if (parsedJson.Message != undefined && parsedJson.Message.length != 0)
                    {
                        alert(parsedJson.Message);
                    }

                }
                else if (serverRequest.status == 200)
                {
                    popupBack.remove();
                    alert(parsedJson.Message);
                    getDoctors(docSearch, serverRequest, filter, sortby, list, popupBack, body);
                }

            }

        }

    }

}

function viewDocDetails(event, details,  docSearch, serverRequest, filter, sortby, list, popupBack, body)
{

    if (event.target.id != "removeButt")
    {

        popupBack.innerHTML = "";

        let detailsBox = document.createElement("div");
        detailsBox.classList.add("addDoc");
        detailsBox.style.width = "400px";
        detailsBox.style.padding = "50px 100px";
        detailsBox.style.alignItems = "start";

        let title = document.createElement("h2");
        title.innerText = "Doctor details";
        title.style.margin = "20px auto";

        let name = document.createElement("h3");
        name.innerText = "Name: " + details.doctorName;

        let id = document.createElement("h3");
        id.innerText = "Id: " + details.docId;

        let phNumber = document.createElement("h3");
        phNumber.innerText = "Phone number: " + details.phNumber;

        let timing = document.createElement("h3");
        timing.innerText = "Timing: " + details.timing;

        let specialist = document.createElement("h3");
        specialist.innerText = "Specialist: " + details.specialist;

        let qualifi = document.createElement("h3");
        qualifi.innerText = "Qualification: " + details.qualification;

        let options = document.createElement("div");
        options.style.width = "300px";
        options.style.justifyContent = "space-between";
        options.style.flexDirection = "row";
        options.style.margin = "20px auto";
            let editDetails = document.createElement("button");
            editDetails.innerText = "Edit details";

            let close = document.createElement("button");
            close.innerText = "Close";

            if (details.doctorStatus == "AVAILABLE")
                options.appendChild(editDetails);
            else
                options.style.justifyContent = "center";
            options.appendChild(close);

        let viewAssignedPati = document.createElement("button");
        viewAssignedPati.innerText = "View assigned patients";
        viewAssignedPati.style.margin = "0px auto";

        detailsBox.appendChild(title);
        detailsBox.appendChild(name);
        detailsBox.appendChild(id);
        detailsBox.appendChild(phNumber);
        detailsBox.appendChild(timing);
        detailsBox.appendChild(specialist);
        detailsBox.appendChild(qualifi);
        detailsBox.appendChild(viewAssignedPati);
        detailsBox.appendChild(options);

        viewAssignedPati.onclick = () => viewAssignedPatients(details, serverRequest, detailsBox, popupBack);
        editDetails.onclick = () => editDocDetails(detailsBox, details, docSearch, serverRequest, filter, sortby, list, popupBack, body);
        close.onclick = () => popupBack.remove();

        popupBack.appendChild(detailsBox);
        body.appendChild(popupBack);

    }

}

function editDocDetails(detailsBox, details, docSearch, serverRequest, filter, sortby, list, popupBack, body)
{

    popupBack.innerHTML = "";
    let form = document.createElement("div");

        let title = document.createElement("h2");
        title.innerText = "Edit doctor";

        // input element one 

        let elementOne = document.createElement("div"); 
        let docName = document.createElement("input");
        let docnameWarn = document.createElement("span");

        docName.setAttribute("type", "text");
        docName.setAttribute("placeholder", "Doctor name");
        docName.value = details.doctorName;
        elementOne.appendChild(docName);
        elementOne.appendChild(docnameWarn);

        // input element two

        let elementTwo = document.createElement("div");
        let phoneNumber = document.createElement("input");
        let phNumWarn = document.createElement("span");

        phoneNumber.setAttribute("type", "number");
        phoneNumber.setAttribute("placeholder", "Phone number");
        phoneNumber.value = details.phNumber;
        elementTwo.appendChild(phoneNumber);
        elementTwo.appendChild(phNumWarn);

        // input element three

        let elementThree = document.createElement("div");
        let specialist = document.createElement("input");
        let specialistWarn = document.createElement("span");

        specialist.setAttribute("type", "text");
        specialist.setAttribute("placeholder", "Specialist type");
        specialist.value = details.specialist;
        elementThree.appendChild(specialist);
        elementThree.appendChild(specialistWarn);

        // input element four

        let elementFour = document.createElement("div");
        let qualification = document.createElement("input");
        let qualiWarn = document.createElement("span");

        qualification.setAttribute("type", "text");
        qualification.setAttribute("placeholder", "Qualification");
        qualification.value = details.qualification;
        elementFour.appendChild(qualification);
        elementFour.appendChild(qualiWarn);

        // input element five

        let elementFive = document.createElement("div");
        let elementFiveInner = document.createElement("div");
            let startAvailableTime = document.createElement("input");
            let amOrPm = document.createElement("select");

            amOrPm.innerHTML = '<option value = "AM">AM</option><option value = "PM">PM</option>';
            startAvailableTime.setAttribute("type", "number");
            startAvailableTime.setAttribute("placeholder", "Starting available time(1 - 12)");
            startAvailableTime.style.marginLeft = "60px";
            startAvailableTime.value = details.timing.substring(0, 2);
            amOrPm.style.marginLeft = "10px";
            amOrPm.value = details.timing.substring(6, 8);
            elementFiveInner.appendChild(startAvailableTime);
            elementFiveInner.appendChild(amOrPm);

        let startAvailableTimeWarn = document.createElement("span");

        elementFive.appendChild(elementFiveInner);
        elementFive.appendChild(startAvailableTimeWarn);

        // input element six

        let elementSix = document.createElement("div");
        let totalAvailTime = document.createElement("input");
        let totalAvailTimeWarn = document.createElement("span");

        totalAvailTime.setAttribute("type", "number");
        totalAvailTime.setAttribute("placeholder", "Total available time(5 - 10)");
        elementSix.appendChild(totalAvailTime);
        elementSix.appendChild(totalAvailTimeWarn);

        // action section

        let actionSection = document.createElement("div");
        let submit = document.createElement("button");
        let cancel = document.createElement("button");

        actionSection.style.flexDirection = "row";
        actionSection.style.width = "300px";
        actionSection.style.justifyContent = "space-between";
        submit.innerText = "Update";
        cancel.innerText = "Cancel";
        actionSection.appendChild(submit);
        actionSection.appendChild(cancel);

    form.appendChild(title);
    form.appendChild(elementOne);
    form.appendChild(elementTwo);
    form.appendChild(elementThree);
    form.appendChild(elementFour);
    form.appendChild(elementFive);
    form.appendChild(elementSix);
    form.appendChild(actionSection);

    form.classList.add("addDoc");
    popupBack.appendChild(form);
    body.appendChild(popupBack);

    submit.onclick = () => validateEditDoctor(details.docId, serverRequest, docName, phoneNumber, specialist, qualification, startAvailableTime, amOrPm,
        totalAvailTime, docnameWarn, phNumWarn, specialistWarn, qualiWarn, startAvailableTimeWarn, totalAvailTimeWarn,
        docSearch, filter, sortby, list, popupBack, body);
    cancel.onclick = () => {
        form.remove();
        popupBack.appendChild(detailsBox);
    }

}

function validateEditDoctor(docId, serverRequest, docName, phoneNumber, specialist, qualification, startAvailableTime, amOrPm,
    totalAvailTime, docnameWarn, phNumWarn, specialistWarn, qualiWarn, startAvailableTimeWarn, totalAvailTimeWarn,
    docSearch, filter, sortby, list, popupBack, body)
{

    docnameWarn.innerText = "";
    phNumWarn.innerText = "";
    specialistWarn.innerText = "";
    qualiWarn.innerText = "";
    startAvailableTimeWarn.innerText = "";
    totalAvailTimeWarn.innerText = "";

    docName.style.borderColor = "";
    phoneNumber.style.borderColor = "";
    specialist.style.borderColor = "";
    qualification.style.borderColor = "";
    startAvailableTime.style.borderColor = "";
    amOrPm.style.borderColor = "";
    totalAvailTime.style.borderColor = "";

    let nameValue = docName.value.trim();
    let phoneNumberValue = phoneNumber.value.trim().replaceAll(" ", "");
    let specialistValue = specialist.value.trim();
    let qualificationValue = qualification.value.trim();
    let startAvailableTimeValue = startAvailableTime.value.trim();
    let amOrPmValue = amOrPm.value.trim();
    let totalAvailTimeValue = totalAvailTime.value.trim();

    if (!/^[a-zA-Z.\s]+$/.test(nameValue) || nameValue.length < 2 || nameValue.length > 80)
    {
        docnameWarn.innerText = "Enter a valid name. Don't use numbers and special characters!";
        docName.style.borderColor = "red";
    }
    else if (!/^\d+$/.test(phoneNumberValue) || phoneNumberValue.length != 10)
    {
        phNumWarn.innerText = "Enter a valid phone number.";
        phoneNumber.style.borderColor = "red";
    }
    else if (!/^[a-zA-Z.\s]+$/.test(specialistValue) || specialistValue.length < 2 || specialistValue.length > 80)
    {
        specialistWarn.innerText = "Enter a valid input. Don't use numbers and special characters!";
        specialist.style.borderColor = "red";
    }
    else if (!/^[a-zA-Z.\s]+$/.test(qualificationValue) || qualificationValue.length < 2 || qualificationValue.length > 80)
    {
        qualiWarn.innerText = "Enter a valid input. Don't use numbers and special characters!";
        qualification.style.borderColor = "red";
    }
    else if (!/^\d+$/.test(startAvailableTimeValue) || startAvailableTimeValue < 1 || startAvailableTimeValue > 12)
    {
        startAvailableTimeWarn.innerText = "Enter a valid input between 1 - 12. Enter numbers only";
        startAvailableTime.style.borderColor = "red";
    }
    else if (!(amOrPmValue == "AM" || amOrPmValue == "PM"))
    {
        startAvailableTimeWarn.innerText = "Enter only AM or PM";
        amOrPm.style.borderColor = "red";
    }
    else if (!/^\d+$/.test(totalAvailTimeValue) || totalAvailTimeValue < 5 || totalAvailTimeValue > 10)
    {
        totalAvailTimeWarn.innerText = "Enter a valid input between 5 - 10. Enter numbers only";
        totalAvailTime.style.borderColor = "red";
    }
    else
    {

        let responseJson = {};
        responseJson.name = nameValue;
        responseJson.docId = docId;
        responseJson.phoneNumber = phoneNumberValue;
        responseJson.specialist = specialistValue;
        responseJson.qualification = qualificationValue;
        responseJson.startAvailableTime = startAvailableTimeValue;
        responseJson.amOrPm = amOrPmValue;
        responseJson.totalAvailTime = totalAvailTimeValue;

        serverRequest.open("POST" ,"http://localhost:8080/HosManSysServlet/EditDoctor");
        serverRequest.setRequestHeader("Content-Type" ,"application/json");
        serverRequest.send(JSON.stringify(responseJson));

        serverRequest.onreadystatechange = () =>
        {

            if (serverRequest.readyState == 4)
            {
                
                let parsedJson = JSON.parse(serverRequest.responseText);
                if (serverRequest.status == 400)
                {

                    if (parsedJson.name != undefined && parsedJson.name.length != 0)
                    {
                        docnameWarn.innerText = parsedJson.name;
                        docName.style.borderColor = "red";
                    }
                    if (parsedJson.phoneNumber != undefined && parsedJson.phoneNumber.length != 0)
                    {
                        phNumWarn.innerText = parsedJson.phoneNumber;
                        phoneNumber.style.borderColor = "red";
                    }
                    if (parsedJson.specialist != undefined && parsedJson.specialist.length != 0)
                    {
                        specialistWarn.innerText = parsedJson.specialist;
                        specialist.style.borderColor = "red";
                    }
                    if (parsedJson.qualification != undefined && parsedJson.qualification.length != 0)
                    {
                        qualiWarn.innerText = parsedJson.qualification;
                        qualification.style.borderColor = "red";
                    }
                    if (parsedJson.startAvailableTime != undefined && parsedJson.startAvailableTime.length != 0)
                    {
                        startAvailableTimeWarn.innerText = parsedJson.startAvailableTime;
                        startAvailableTime.style.borderColor = "red";
                    }
                    if (parsedJson.totalAvailTime != undefined && parsedJson.totalAvailTime.length != 0)
                    {
                        totalAvailTimeWarn.innerText = parsedJson.totalAvailTime;
                        totalAvailTime.style.borderColor = "red";
                    }
                    if (parsedJson.Message != undefined && parsedJson.Message.length != 0)
                    {
                        alert(parsedJson.Message);
                    }

                }
                else if (serverRequest.status == 200)
                {
                    popupBack.remove();
                    alert(parsedJson.Message);
                    getDoctors(docSearch, serverRequest, filter, sortby, list, popupBack, body);
                }

            }

        }

    }

}

function viewAssignedPatients(details, serverRequest, detailsBox, popupBack)
{

    let responseJson = {};
    responseJson.searchKey = "";
    responseJson.treatedBy = details.docId;
    responseJson.filter = "all";
    responseJson.sortby = "firstAdmitted";

    serverRequest.open("POST" ,"http://localhost:8080/HosManSysServlet/GetPatients");
    serverRequest.setRequestHeader("Content-Type" ,"application/json");
    serverRequest.send(JSON.stringify(responseJson));

    serverRequest.onreadystatechange = () =>
    {

        if (serverRequest.readyState == 4)
        {

            let listCover = document.createElement("div");
            listCover.style.display = "flex";
            listCover.style.flexDirection = "column";
            listCover.style.alignItems = "center";
            listCover.style.background = "#62deff";
            listCover.style.borderRadius = "10px";
            listCover.style.border = "3px solid";
            listCover.style.padding = "0px 20px";

            let title = document.createElement("h2");
            title.innerText = "Assigned patients";

            listCover.appendChild(title);

            let list = document.createElement("div");
            list.style.width = "680px";
            list.style.borderRadius = "10px";
            list.style.border = "2px solid";
            list.classList.add("list");
            detailsBox.remove();
            if (serverRequest.status == 200)
            {

                let parsedJson = JSON.parse(serverRequest.responseText.substring(0, serverRequest.responseText.indexOf("]") + 1));

                if (parsedJson.length == 0)
                {
                    list.innerHTML = "<h2>No patients assigned!!</h2>";
                }
                else
                {

                    for (let i = 0; i < parsedJson.length; i ++)
                    {

                        let patientDetails = document.createElement("div");
                        patientDetails.classList.add("details");

                        let docIcon = document.createElement("img");
                        docIcon.classList.add("listIcon");
                        docIcon.setAttribute("src", "Assets/patient.svg");

                        let nameDetails = document.createElement("div");
                        nameDetails.style.width = "210px";
                            let patientName = document.createElement("h3");
                            patientName.innerText = parsedJson[i].patientName;
                            patientName.style.margin = "5px";

                            let admissionNum = document.createElement("h5");
                            admissionNum.innerText = "Admission no: " + ((parsedJson[i].admissionNum.length > 10)? parsedJson[i].admissionNum.substring(0, 15) + "...": parsedJson[i].admissionNum);
                            admissionNum.style.margin = "5px";

                            nameDetails.appendChild(patientName);
                            nameDetails.appendChild(admissionNum);

                        let otherDetails = document.createElement("div");
                        otherDetails.style.marginLeft = "10px";
                        otherDetails.style.width = "210px";
                            let phNumber = document.createElement("h4");
                            phNumber.innerText = "Ph no: " + parsedJson[i].phNumber;
                            phNumber.style.margin = "5px";

                            let timing = document.createElement("h5");
                            timing.innerText = "Disease: " + parsedJson[i].disease;
                            timing.style.margin = "5px";

                            otherDetails.appendChild(phNumber);
                            otherDetails.appendChild(timing);

                        patientDetails.appendChild(docIcon);
                        patientDetails.appendChild(nameDetails);
                        patientDetails.appendChild(otherDetails);

                        list.appendChild(patientDetails);
                        let status = document.createElement("h3");
                        status.style.marginLeft = "55px";

                        if (parsedJson[i].admissionStatus == "ADMITTED")
                            status.innerText = "Admitted";
                        else
                            status.innerText = "Discharged";

                        patientDetails.appendChild(status);

                    }

                }
                let close = document.createElement("button");
                close.innerText = "Close";
                close.style.margin = "20px";
                close.style.fontSize = "large";
                close.style.padding = "10px 20px";

                listCover.appendChild(list);
                listCover.appendChild(close);
                popupBack.appendChild(listCover);

                close.onclick = () =>
                {
                    popupBack.appendChild(detailsBox);
                    listCover.remove();
                }

            }
            else if (serverRequest.status == 400)
            {
                alert(JSON.parse(serverRequest.responseText).Message);
            }
        }
    }
}

// ---------- Patient section ----------

function getPatients(patiSearch, filter, sortby, list, popupBack, body)
{

    let serverRequest = new XMLHttpRequest();
    let responseJson = {};
    responseJson.searchKey = (patiSearch.value == undefined)? "": patiSearch.value.trim();
    responseJson.treatedBy = "all";
    responseJson.filter = filter.value;
    responseJson.sortby = sortby.value;

    serverRequest.open("POST" ,"http://localhost:8080/HosManSysServlet/GetPatients");
    serverRequest.setRequestHeader("Content-Type" ,"application/json");
    serverRequest.send(JSON.stringify(responseJson));

    serverRequest.onreadystatechange = () =>
    {

        if (serverRequest.readyState == 4)
        {

            list.innerHTML = "";
            if (serverRequest.status == 200)
            {

                let parsedJson = JSON.parse(serverRequest.responseText.substring(0, serverRequest.responseText.indexOf("]") + 1));
                document.getElementById("totalPatients").innerText = "Number of admitted patients: " 
                    + serverRequest.responseText.substring(serverRequest.responseText.indexOf("]") + 1, serverRequest.responseText.length);

                if (parsedJson.length == 0)
                {
                    list.innerHTML = "<h2>No patients available!!</h2>";
                }
                else
                {

                    for (let i = 0; i < parsedJson.length; i ++)
                    {

                        let patientDetails = document.createElement("div");
                        patientDetails.classList.add("details");

                        let docIcon = document.createElement("img");
                        docIcon.classList.add("listIcon");
                        docIcon.setAttribute("src", "Assets/patient.svg");

                        let nameDetails = document.createElement("div");
                        nameDetails.style.width = "210px";
                            let patientName = document.createElement("h3");
                            patientName.innerText = parsedJson[i].patientName;
                            patientName.style.margin = "5px";

                            let admissionNum = document.createElement("h5");
                            admissionNum.innerText = "Admission no: " + ((parsedJson[i].admissionNum.length > 10)? parsedJson[i].admissionNum.substring(0, 15) + "...": parsedJson[i].admissionNum);
                            admissionNum.style.margin = "5px";

                            nameDetails.appendChild(patientName);
                            nameDetails.appendChild(admissionNum);

                        let otherDetails = document.createElement("div");
                        otherDetails.style.marginLeft = "10px";
                        otherDetails.style.width = "210px";
                            let phNumber = document.createElement("h4");
                            phNumber.innerText = "Ph no: " + parsedJson[i].phNumber;
                            phNumber.style.margin = "5px";

                            let timing = document.createElement("h5");
                            timing.innerText = "Disease: " + parsedJson[i].disease;
                            timing.style.margin = "5px";

                            otherDetails.appendChild(phNumber);
                            otherDetails.appendChild(timing);

                        patientDetails.appendChild(docIcon);
                        patientDetails.appendChild(nameDetails);
                        patientDetails.appendChild(otherDetails);

                        list.appendChild(patientDetails);

                        let removeButt = document.createElement("button");
                        removeButt.classList.add("removeButt");
                        removeButt.id = "removeButt";
                        removeButt.innerText = "Discharge";

                        if (parsedJson[i].admissionStatus == "ADMITTED")
                        {

                            let status = document.createElement("h3");
                            status.innerText = "Admitted";
                            status.style.marginLeft = "55px";

                            patientDetails.appendChild(status);
                            patientDetails.onmouseover = () =>
                            {
                                status.remove();
                                patientDetails.appendChild(removeButt);
                            };
    
                            patientDetails.onmouseleave = () =>
                            {
                                removeButt.remove();
                                patientDetails.appendChild(status);
                            }

                        }
                        else
                        {
                            let status = document.createElement("h3");
                            status.innerText = "Discharged";
                            status.style.marginLeft = "55px";

                            patientDetails.appendChild(status);
                        }

                        patientDetails.onclick = (event) => viewPatientDetails(event, parsedJson[i], patiSearch, serverRequest, filter, sortby, list, popupBack, body);
                        removeButt.onclick = () => dischargePatient(parsedJson[i], patiSearch, serverRequest, filter, sortby, list, popupBack, body);

                    }

                }

            }
            else if (serverRequest.status == 400)
            {
                alert(JSON.parse(serverRequest.responseText).Message);
            }
        }
    }
}

function dischargePatient(patientDetails, patiSearch, serverRequest, filter, sortby, list, popupBack, body)
{

    if (patientDetails.feePaid <= 0)
    {

        popupBack.innerHTML = "";
        let form = document.createElement("div");
        form.style.height = "340px";

        let title = document.createElement("h2");
        title.innerText = "Set payment to continue";

        // input element one 

        let admissionNum = document.createElement("h2");
        admissionNum.innerText = "Patient's admission number: " + patientDetails.admissionNum;

        // input element one

        let elementOne = document.createElement("div");
        let lable = document.createElement("h3");
        let payment = document.createElement("input");
        let paymentWarn = document.createElement("span");

        lable.innerText = "Enter payment amount in rupees"
        payment.setAttribute("type", "number");
        payment.setAttribute("placeholder", "Payment amount");
        payment.value = patientDetails.feePaid;
        elementOne.appendChild(lable);
        elementOne.appendChild(payment);
        elementOne.appendChild(paymentWarn);

        // action section

        let actionSection = document.createElement("div");
        let submit = document.createElement("button");
        let cancel = document.createElement("button");

        actionSection.style.flexDirection = "row";
        actionSection.style.width = "300px";
        actionSection.style.justifyContent = "space-between";
        submit.innerText = "Update";
        cancel.innerText = "Cancel";
        actionSection.appendChild(submit);
        actionSection.appendChild(cancel);

        form.appendChild(title);
        form.appendChild(admissionNum);
        form.appendChild(elementOne);
        form.appendChild(actionSection);

        form.classList.add("addDoc");
        popupBack.appendChild(form);
        body.appendChild(popupBack);

        submit.onclick = () =>
        {

            if (payment.value > 0)
            {
                paymentWarn.innerText = "";
                payment.style.borderColor = "";

                let responseJson = {};

                responseJson.admissionNum = patientDetails.admissionNum;
                responseJson.payment = payment.value;

                serverRequest.open("POST" ,"http://localhost:8080/HosManSysServlet/SetPayment");
                serverRequest.setRequestHeader("Content-Type" ,"application/json");
                serverRequest.send(JSON.stringify(responseJson));

                serverRequest.onreadystatechange = () =>
                {

                    if (serverRequest.readyState == 4)
                    {
                
                        let parsedJson = JSON.parse(serverRequest.responseText);
                        if (serverRequest.status == 400)
                        {
                            if (parsedJson.payment != undefined && parsedJson.payment.length != 0)
                            {
                                paymentWarn.innerText = parsedJson.payment;
                                payment.style.borderColor = "red";
                            }
                            if (parsedJson.Message != undefined && parsedJson.Message.length != 0)
                            {
                                alert(parsedJson.Message);
                            }
                        }
                        else if (serverRequest.status == 200)
                        {
                            popupBack.remove();
                            alert(parsedJson.Message);
                            patientDetails.feePaid = payment.value;
                            dischargePatient(patientDetails, patiSearch, serverRequest, filter, sortby, list, popupBack, body);
                        }

                    }

                }
                
            }
            else
            {
                paymentWarn.innerText = "Enter a valid amount!";
                payment.style.borderColor = "red";
            }

        }

        cancel.onclick = () => {
            form.remove();
            popupBack.appendChild(detailsBox);
    
        }

    }
    else
    {

        popupBack.innerHTML = "";
        let conformationBox = document.createElement("div");
        conformationBox.classList.add("confirmBox");
            let confirmId = document.createElement("h2");
            confirmId.innerText = "Patient admission number: " + patientDetails.admissionNum;

            let confirmText = document.createElement("h2");
            confirmText.style.margin = "0px 0px 20px 0px";
            confirmText.innerText = "Are you sure to discharge the patient?";

            let optionDiv = document.createElement("div");
                let yesButt = document.createElement("button");
                yesButt.innerText = "Yes";

                let cancelButt = document.createElement("button");
                cancelButt.innerText = "Cancel";

                optionDiv.appendChild(yesButt);
                optionDiv.appendChild(cancelButt);

            conformationBox.appendChild(confirmId);
            conformationBox.appendChild(confirmText);
            conformationBox.appendChild(optionDiv);

        popupBack.appendChild(conformationBox);

        cancelButt.onclick = () => popupBack.remove();
        yesButt.onclick = () =>
        {

            let responseJson = {};
            responseJson.admissionNum = patientDetails.admissionNum;

            serverRequest.open("POST" ,"http://localhost:8080/HosManSysServlet/DischargePatient");
            serverRequest.setRequestHeader("Content-Type" ,"application/json");
            serverRequest.send(JSON.stringify(responseJson));

            serverRequest.onreadystatechange = () =>
            {
            
                if (serverRequest.readyState == 4)
                {

                    popupBack.remove();
                    let message = JSON.parse(serverRequest.responseText);
                    if (serverRequest.status == 400)
                    {
                        alert(message.Message);
                    }
                    else if (serverRequest.status == 200)
                    {
                        alert(message.Message);
                        getPatients(patiSearch, filter, sortby, list, popupBack, body);
                    }

                }

            };

        };
        body.appendChild(popupBack);
    }
}

function admitPatient(patiSearch, serverRequest, filter, sortby, list, popupBack, body)
{

    popupBack.innerHTML = "";
    let form = document.createElement("div");
    form.style.height = "650px";

        let title = document.createElement("h2");
        title.innerText = "Admit patient";

        // input element one 

        let elementOne = document.createElement("div"); 
        let patiName = document.createElement("input");
        let patiNameWarn = document.createElement("span");

        patiName.setAttribute("type", "text");
        patiName.setAttribute("placeholder", "Patient name");
        elementOne.appendChild(patiName);
        elementOne.appendChild(patiNameWarn);

        // input element two

        let elementTwo = document.createElement("div");
        let phoneNumber = document.createElement("input");
        let phNumWarn = document.createElement("span");

        phoneNumber.setAttribute("type", "number");
        phoneNumber.setAttribute("placeholder", "Phone number");
        elementTwo.appendChild(phoneNumber);
        elementTwo.appendChild(phNumWarn);

        // input element three

        let elementThree = document.createElement("div");
        let patiAge = document.createElement("input");
        let patiAgeWarn = document.createElement("span");

        patiAge.setAttribute("type", "number");
        patiAge.setAttribute("placeholder", "Patient age(1 - 150)");
        elementThree.appendChild(patiAge);
        elementThree.appendChild(patiAgeWarn);

        // input element four

        let elementFour = document.createElement("div");
        let elementFourInner = document.createElement("div");
            let genderLable = document.createElement("lable");
            let gender = document.createElement("select");

            gender.innerHTML = '<option value="male">Male</option><option value="female">Female</option><option value="other">Other</option>';
            genderLable.innerText = "Gender: ";
            genderLable.style.fontSize = "1.25rem";
            genderLable.style.fontWeight = "bolder";
            gender.style.marginLeft = "10px";
            elementFourInner.appendChild(genderLable);
            elementFourInner.appendChild(gender);

        let genderWarn = document.createElement("span");

        elementFour.appendChild(elementFourInner);
        elementFour.appendChild(genderWarn);

        // input element five

        let elementFive = document.createElement("div");
        let disease = document.createElement("input");
        let diseaseWarn = document.createElement("span");

        disease.setAttribute("type", "text");
        disease.setAttribute("placeholder", "Disease");
        elementFive.appendChild(disease);
        elementFive.appendChild(diseaseWarn);

        // action section

        let actionSection = document.createElement("div");
        let submit = document.createElement("button");
        let cancel = document.createElement("button");

        actionSection.style.flexDirection = "row";
        actionSection.style.width = "300px";
        actionSection.style.justifyContent = "space-between";
        submit.innerText = "Admit";
        cancel.innerText = "Cancel";
        actionSection.appendChild(submit);
        actionSection.appendChild(cancel);

    form.appendChild(title);
    form.appendChild(elementOne);
    form.appendChild(elementTwo);
    form.appendChild(elementThree);
    form.appendChild(elementFour);
    form.appendChild(elementFive);
    form.appendChild(actionSection);

    form.classList.add("addDoc");
    popupBack.appendChild(form);
    body.appendChild(popupBack);

    submit.onclick = () => validateAdmitPatient(serverRequest, patiName, phoneNumber, patiAge, gender,
        disease, patiNameWarn, phNumWarn, patiAgeWarn, genderWarn, diseaseWarn,
        patiSearch, filter, sortby, list, popupBack, body);
    cancel.onclick = () => popupBack.remove();
    
}

function validateAdmitPatient(serverRequest, patiName, phoneNumber, age, gender,
    disease, patiNameWarn, phNumWarn, ageWarn, genderWarn, diseaseWarn,
    patiSearch, filter, sortby, list, popupBack, body)
{

    patiNameWarn.innerText = "";
    phNumWarn.innerText = "";
    ageWarn.innerText = "";
    genderWarn.innerText = "";
    diseaseWarn.innerText = "";

    patiName.style.borderColor = "";
    phoneNumber.style.borderColor = "";
    age.style.borderColor = "";
    gender.style.borderColor = "";
    disease.style.borderColor = "";

    let nameValue = patiName.value.trim();
    let phoneNumberValue = phoneNumber.value.trim().replaceAll(" ", "");
    let ageValue = age.value.trim();
    let genderValue = gender.value.trim();
    let diseaseValue = disease.value.trim();

    if (!/^[a-zA-Z.\s]+$/.test(nameValue) || nameValue.length < 2 || nameValue.length > 80)
    {
        patiNameWarn.innerText = "Enter a valid name. Don't use numbers and special characters!";
        patiName.style.borderColor = "red";
    }
    else if (!/^\d+$/.test(phoneNumberValue) || phoneNumberValue.length != 10)
    {
        phNumWarn.innerText = "Enter a valid phone number.";
        phoneNumber.style.borderColor = "red";
    }
    else if (ageValue > 150 || ageValue < 1)
    {
        ageWarn.innerText = "Enter a valid age between 1 - 150";
        age.style.borderColor = "red";
    }
    else if (!(genderValue == "male" || genderValue == "female" || genderValue == "other"))
    {
        genderWarn.innerText = "Enter only Male or Female or Other";
        gender.style.borderColor = "red";
    }
    else if (!/^[a-zA-Z.\s]+$/.test(diseaseValue) || diseaseValue.length < 2 || diseaseValue.length > 80)
    {
        diseaseWarn.innerText = "Enter a valid input. Don't use numbers and special characters!";
        disease.style.borderColor = "red";
    }
    else
    {

        let responseJson = {};
        responseJson.name = nameValue;
        responseJson.phoneNumber = phoneNumberValue;
        responseJson.age = ageValue;
        responseJson.gender = genderValue;
        responseJson.disease = diseaseValue;

        serverRequest.open("POST" ,"http://localhost:8080/HosManSysServlet/AdmitPatient");
        serverRequest.setRequestHeader("Content-Type" ,"application/json");
        serverRequest.send(JSON.stringify(responseJson));

        serverRequest.onreadystatechange = () =>
        {

            if (serverRequest.readyState == 4)
            {
                
                let parsedJson = JSON.parse(serverRequest.responseText);
                if (serverRequest.status == 400)
                {

                    if (parsedJson.name != undefined && parsedJson.name.length != 0)
                    {
                        patiNameWarn.innerText = parsedJson.name;
                        patiName.style.borderColor = "red";
                    }
                    if (parsedJson.phoneNumber != undefined && parsedJson.phoneNumber.length != 0)
                    {
                        phNumWarn.innerText = parsedJson.phoneNumber;
                        phoneNumber.style.borderColor = "red";
                    }
                    if (parsedJson.age != undefined && parsedJson.age.length != 0)
                    {
                        ageWarn.innerText = parsedJson.age;
                        age.style.borderColor = "red";
                    }
                    if (parsedJson.gender != undefined && parsedJson.gender.length != 0)
                    {
                        genderWarn.innerText = parsedJson.gender;
                        gender.style.borderColor = "red";
                    }
                    if (parsedJson.disease != undefined && parsedJson.disease.length != 0)
                    {
                        diseaseWarn.innerText = parsedJson.disease;
                        disease.style.borderColor = "red";
                    }
                    if (parsedJson.Message != undefined && parsedJson.Message.length != 0)
                    {
                        alert(parsedJson.Message);
                    }

                }
                else if (serverRequest.status == 200)
                {
                    popupBack.remove();
                    alert(parsedJson.Message);
                    getPatients(patiSearch, filter, sortby, list, popupBack, body);
                }

            }

        }

    }

}

function viewPatientDetails(events, details, patiSearch, serverRequest, filter, sortby, list, popupBack, body)
{

    if (events.target.id != "removeButt")
    {

        popupBack.innerHTML = "";

        let detailsBox = document.createElement("div");
        detailsBox.classList.add("addDoc");
        detailsBox.style.width = "400px";
        detailsBox.style.height = "800px";
        detailsBox.style.marginTop = "100px";
        detailsBox.style.padding = "50px 100px";
        detailsBox.style.alignItems = "start";

        let title = document.createElement("h2");
        title.innerText = "Patient details";
        title.style.margin = "0px auto 20px";

        let name = document.createElement("h3");
        name.style.margin = "10px 0px";
        name.innerText = "Name: " + details.patientName;

        let admissionNum = document.createElement("h3");
        admissionNum.style.margin = "10px 0px";
        admissionNum.innerText = "Admission number: " + details.admissionNum;

        let phNumber = document.createElement("h3");
        phNumber.style.margin = "10px 0px";
        phNumber.innerText = "Phone number: " + details.phNumber;

        let age = document.createElement("h3");
        age.style.margin = "10px 0px";
        age.innerText = "Age: " + details.age;

        let gender = document.createElement("h3");
        gender.style.margin = "10px 0px";
        gender.innerText = "Gender: " + details.gender;

        let disease = document.createElement("h3");
        disease.style.margin = "10px 0px";
        disease.innerText = "Disease: " + details.disease;

        let treatedBy = document.createElement("h3");
        treatedBy.style.margin = "10px 0px";
        treatedBy.innerText = "Treated by: " + details.treatedBy;

        let addmittedDate = document.createElement("h3");
        addmittedDate.style.margin = "10px 0px";
        addmittedDate.innerText = "Admitted date: " + details.addmittedDate;

        let dischargedDate = document.createElement("h3");
        dischargedDate.style.margin = "10px 0px";
        dischargedDate.innerText = "Discharged date: " + details.dischargedDate;

        let feePaid = document.createElement("h3");
        feePaid.style.margin = "10px 0px";
        feePaid.innerText = "Fee paid(in rupees): " + ((details.feePaid == 0)? "Yet to pay": details.feePaid);

        let assignForDoc = document.createElement("button");
        assignForDoc.style.margin = "0px auto";
        assignForDoc.innerText = (details.treatedBy == "Yet to treat")? "Assign for a doctor": "Reassign for a doctor";

        let options = document.createElement("div");
        options.style.width = "400px";
        options.style.justifyContent = "space-between";
        options.style.flexDirection = "row";
        options.style.margin = "20px auto";
            let editDetails = document.createElement("button");
            editDetails.style.margin = "0px";
            editDetails.innerText = "Edit details";

            let setPayment = document.createElement("button");
            setPayment.style.margin = "0px";
            setPayment.innerText = "Set Payment";

            let close = document.createElement("button");
            close.style.margin = "0px";
            close.innerText = "Close";

        detailsBox.appendChild(title);
        detailsBox.appendChild(name);
        detailsBox.appendChild(admissionNum);
        detailsBox.appendChild(phNumber);
        detailsBox.appendChild(age);
        detailsBox.appendChild(gender);
        detailsBox.appendChild(disease);
        detailsBox.appendChild(treatedBy);
        detailsBox.appendChild(addmittedDate);
        detailsBox.appendChild(dischargedDate);
        detailsBox.appendChild(feePaid);

        if (details.admissionStatus == "ADMITTED")
        {
            detailsBox.appendChild(assignForDoc);
            options.appendChild(editDetails);
            options.appendChild(setPayment);
        }
        else
            options.style.justifyContent = "center";
        options.appendChild(close);
        detailsBox.appendChild(options);

        assignForDoc.onclick = () => assignForDoctor(detailsBox, details, patiSearch, serverRequest, filter, sortby, list, popupBack, body);
        editDetails.onclick = () => editPatientDetails(detailsBox, details, patiSearch, serverRequest, filter, sortby, list, popupBack, body);
        setPayment.onclick = () => setPaymentForPatient(detailsBox, details, patiSearch, serverRequest, filter, sortby, list, popupBack, body);
        close.onclick = () => popupBack.remove();

        popupBack.appendChild(detailsBox);
        body.appendChild(popupBack);

    }

}

function editPatientDetails(detailsBox, details, patiSearch, serverRequest, filter, sortby, list, popupBack, body)
{

    popupBack.innerHTML = "";
    let form = document.createElement("div");
    form.style.height = "650px";

        let title = document.createElement("h2");
        title.innerText = "Edit patient";

        // input element one 

        let elementOne = document.createElement("div"); 
        let patiName = document.createElement("input");
        let patiNameWarn = document.createElement("span");

        patiName.setAttribute("type", "text");
        patiName.setAttribute("placeholder", "Patient name");
        patiName.value = details.patientName;
        elementOne.appendChild(patiName);
        elementOne.appendChild(patiNameWarn);

        // input element two

        let elementTwo = document.createElement("div");
        let phoneNumber = document.createElement("input");
        let phNumWarn = document.createElement("span");

        phoneNumber.setAttribute("type", "number");
        phoneNumber.setAttribute("placeholder", "Phone number");
        phoneNumber.value = details.phNumber;
        elementTwo.appendChild(phoneNumber);
        elementTwo.appendChild(phNumWarn);

        // input element three

        let elementThree = document.createElement("div");
        let patiAge = document.createElement("input");
        let patiAgeWarn = document.createElement("span");

        patiAge.setAttribute("type", "number");
        patiAge.setAttribute("placeholder", "Patient age(1 - 150)");
        patiAge.value = details.age;
        elementThree.appendChild(patiAge);
        elementThree.appendChild(patiAgeWarn);

        // input element four

        let elementFour = document.createElement("div");
        let elementFourInner = document.createElement("div");
            let genderLable = document.createElement("lable");
            let gender = document.createElement("select");

            gender.innerHTML = '<option value="male">Male</option><option value="female">Female</option><option value="other">Other</option>';
            genderLable.innerText = "Gender: ";
            genderLable.style.fontSize = "1.25rem";
            genderLable.style.fontWeight = "bolder";
            gender.style.marginLeft = "10px";
            gender.value = details.gender.toLowerCase();
            elementFourInner.appendChild(genderLable);
            elementFourInner.appendChild(gender);

        let genderWarn = document.createElement("span");

        elementFour.appendChild(elementFourInner);
        elementFour.appendChild(genderWarn);

        // input element five

        let elementFive = document.createElement("div");
        let disease = document.createElement("input");
        let diseaseWarn = document.createElement("span");

        disease.setAttribute("type", "text");
        disease.setAttribute("placeholder", "Disease");
        disease.value = details.disease;
        elementFive.appendChild(disease);
        elementFive.appendChild(diseaseWarn);

        // action section

        let actionSection = document.createElement("div");
        let submit = document.createElement("button");
        let cancel = document.createElement("button");

        actionSection.style.flexDirection = "row";
        actionSection.style.width = "300px";
        actionSection.style.justifyContent = "space-between";
        submit.innerText = "Update";
        cancel.innerText = "Cancel";
        actionSection.appendChild(submit);
        actionSection.appendChild(cancel);

    form.appendChild(title);
    form.appendChild(elementOne);
    form.appendChild(elementTwo);
    form.appendChild(elementThree);
    form.appendChild(elementFour);
    form.appendChild(elementFive);
    form.appendChild(actionSection);

    form.classList.add("addDoc");
    popupBack.appendChild(form);
    body.appendChild(popupBack);

    submit.onclick = () => validateEditPatient(details.admissionNum, serverRequest, patiName, phoneNumber, patiAge, gender,
        disease, patiNameWarn, phNumWarn, patiAgeWarn, genderWarn, diseaseWarn,
        patiSearch, filter, sortby, list, popupBack, body);
    cancel.onclick = () => {
        form.remove();
        popupBack.appendChild(detailsBox);

    }

}

function validateEditPatient(admissionNum, serverRequest, patiName, phoneNumber, age, gender,
    disease, patiNameWarn, phNumWarn, ageWarn, genderWarn, diseaseWarn,
    patiSearch, filter, sortby, list, popupBack, body)
{

    patiNameWarn.innerText = "";
    phNumWarn.innerText = "";
    ageWarn.innerText = "";
    genderWarn.innerText = "";
    diseaseWarn.innerText = "";

    patiName.style.borderColor = "";
    phoneNumber.style.borderColor = "";
    age.style.borderColor = "";
    gender.style.borderColor = "";
    disease.style.borderColor = "";

    let nameValue = patiName.value.trim();
    let phoneNumberValue = phoneNumber.value.trim().replaceAll(" ", "");
    let ageValue = age.value.trim();
    let genderValue = gender.value.trim();
    let diseaseValue = disease.value.trim();

    if (!/^[a-zA-Z.\s]+$/.test(nameValue) || nameValue.length < 2 || nameValue.length > 80)
    {
        patiNameWarn.innerText = "Enter a valid name. Don't use numbers and special characters!";
        patiName.style.borderColor = "red";
    }
    else if (!/^\d+$/.test(phoneNumberValue) || phoneNumberValue.length != 10)
    {
        phNumWarn.innerText = "Enter a valid phone number.";
        phoneNumber.style.borderColor = "red";
    }
    else if (ageValue > 150 || ageValue < 1)
    {
        ageWarn.innerText = "Enter a valid age between 1 - 150";
        age.style.borderColor = "red";
    }
    else if (!(genderValue == "male" || genderValue == "female" || genderValue == "other"))
    {
        genderWarn.innerText = "Enter only Male or Female or Other";
        gender.style.borderColor = "red";
    }
    else if (!/^[a-zA-Z.\s]+$/.test(diseaseValue) || diseaseValue.length < 2 || diseaseValue.length > 80)
    {
        diseaseWarn.innerText = "Enter a valid input. Don't use numbers and special characters!";
        disease.style.borderColor = "red";
    }
    else
    {

        let responseJson = {};
        responseJson.admissionNum = admissionNum;
        responseJson.name = nameValue;
        responseJson.phoneNumber = phoneNumberValue;
        responseJson.age = ageValue;
        responseJson.gender = genderValue;
        responseJson.disease = diseaseValue;

        serverRequest.open("POST" ,"http://localhost:8080/HosManSysServlet/EditPatient");
        serverRequest.setRequestHeader("Content-Type" ,"application/json");
        serverRequest.send(JSON.stringify(responseJson));

        serverRequest.onreadystatechange = () =>
        {

            if (serverRequest.readyState == 4)
            {
                
                let parsedJson = JSON.parse(serverRequest.responseText);
                if (serverRequest.status == 400)
                {

                    if (parsedJson.name != undefined && parsedJson.name.length != 0)
                    {
                        patiNameWarn.innerText = parsedJson.name;
                        patiName.style.borderColor = "red";
                    }
                    if (parsedJson.phoneNumber != undefined && parsedJson.phoneNumber.length != 0)
                    {
                        phNumWarn.innerText = parsedJson.phoneNumber;
                        phoneNumber.style.borderColor = "red";
                    }
                    if (parsedJson.age != undefined && parsedJson.age.length != 0)
                    {
                        ageWarn.innerText = parsedJson.age;
                        age.style.borderColor = "red";
                    }
                    if (parsedJson.gender != undefined && parsedJson.gender.length != 0)
                    {
                        genderWarn.innerText = parsedJson.gender;
                        gender.style.borderColor = "red";
                    }
                    if (parsedJson.disease != undefined && parsedJson.disease.length != 0)
                    {
                        diseaseWarn.innerText = parsedJson.disease;
                        disease.style.borderColor = "red";
                    }
                    if (parsedJson.Message != undefined && parsedJson.Message.length != 0)
                    {
                        alert(parsedJson.Message);
                    }

                }
                else if (serverRequest.status == 200)
                {
                    popupBack.remove();
                    alert(parsedJson.Message);
                    getPatients(patiSearch, filter, sortby, list, popupBack, body);
                }

            }

        }

    }

}

function setPaymentForPatient(detailsBox, details, patiSearch, serverRequest, filter, sortby, list, popupBack, body)
{

    popupBack.innerHTML = "";
    let form = document.createElement("div");
    form.style.height = "340px";

        let title = document.createElement("h2");
        title.innerText = "Set payment";

        // input element one 

        let admissionNum = document.createElement("h2");
        admissionNum.innerText = "Patient's admission number: " + details.admissionNum;

        // input element one

        let elementOne = document.createElement("div");
        let lable = document.createElement("h3");
        let payment = document.createElement("input");
        let paymentWarn = document.createElement("span");

        lable.innerText = "Enter payment amount in rupees"
        payment.setAttribute("type", "number");
        payment.setAttribute("placeholder", "Payment amount");
        payment.value = details.feePaid;
        elementOne.appendChild(lable);
        elementOne.appendChild(payment);
        elementOne.appendChild(paymentWarn);

        // action section

        let actionSection = document.createElement("div");
        let submit = document.createElement("button");
        let cancel = document.createElement("button");

        actionSection.style.flexDirection = "row";
        actionSection.style.width = "300px";
        actionSection.style.justifyContent = "space-between";
        submit.innerText = "Update";
        cancel.innerText = "Cancel";
        actionSection.appendChild(submit);
        actionSection.appendChild(cancel);

        form.appendChild(title);
        form.appendChild(admissionNum);
        form.appendChild(elementOne);
        form.appendChild(actionSection);

        form.classList.add("addDoc");
        popupBack.appendChild(form);
        body.appendChild(popupBack);

        submit.onclick = () =>
        {

            if (payment.value > 0)
            {
                paymentWarn.innerText = "";
                payment.style.borderColor = "";

                let responseJson = {};

                responseJson.admissionNum = details.admissionNum;
                responseJson.payment = payment.value;

                serverRequest.open("POST" ,"http://localhost:8080/HosManSysServlet/SetPayment");
                serverRequest.setRequestHeader("Content-Type" ,"application/json");
                serverRequest.send(JSON.stringify(responseJson));

                serverRequest.onreadystatechange = () =>
                {

                    if (serverRequest.readyState == 4)
                    {
                
                        let parsedJson = JSON.parse(serverRequest.responseText);
                        if (serverRequest.status == 400)
                        {
                            if (parsedJson.payment != undefined && parsedJson.payment.length != 0)
                            {
                                paymentWarn.innerText = parsedJson.payment;
                                payment.style.borderColor = "red";
                            }
                            if (parsedJson.Message != undefined && parsedJson.Message.length != 0)
                            {
                                alert(parsedJson.Message);
                            }
                        }
                        else if (serverRequest.status == 200)
                        {
                            popupBack.remove();
                            alert(parsedJson.Message);
                            getPatients(patiSearch, filter, sortby, list, popupBack, body);
                        }
                    }
                }
            }
            else
            {
                paymentWarn.innerText = "Enter a valid amount!";
                payment.style.borderColor = "red";
            }

        }

        cancel.onclick = () => {
            form.remove();
            popupBack.appendChild(detailsBox);
    
        }

}

function assignForDoctor(detailsBox, details, patiSearch, serverRequest, filter, sortby, list, popupBack, body)
{

    popupBack.innerHTML = "";
    let form = document.createElement("div");
    form.style.height = "340px";

        let title = document.createElement("h2");
        title.innerText = (details.treatedBy == "Yet to treat")? "Assign for a doctor": "Reassign for a doctor";

        // input element one 

        let admissionNum = document.createElement("h2");
        admissionNum.innerText = "Patient's admission number: " + details.admissionNum;

        // input element one

        let elementOne = document.createElement("div");
        let lable = document.createElement("h3");
        let docId = document.createElement("input");
        let docIdWarn = document.createElement("span");

        lable.innerText = "Enter doctor id";
        docId.setAttribute("type", "text");
        docId.setAttribute("placeholder", "Doctor id");
        docId.value = (details.treatedBy == "Yet to treat")? "": details.treatedBy;
        elementOne.appendChild(lable);
        elementOne.appendChild(docId);
        elementOne.appendChild(docIdWarn);

        // action section

        let actionSection = document.createElement("div");
        let submit = document.createElement("button");
        let cancel = document.createElement("button");

        actionSection.style.flexDirection = "row";
        actionSection.style.width = "300px";
        actionSection.style.justifyContent = "space-between";
        submit.innerText = (details.treatedBy == "Yet to treat")? "Assign": "Reassign";
        cancel.innerText = "Cancel";
        actionSection.appendChild(submit);
        actionSection.appendChild(cancel);

        form.appendChild(title);
        form.appendChild(admissionNum);
        form.appendChild(elementOne);
        form.appendChild(actionSection);

        form.classList.add("addDoc");
        popupBack.appendChild(form);
        body.appendChild(popupBack);

        submit.onclick = () =>
        {

            if (docId.value != "")
            {
                docIdWarn.innerText = "";
                docId.style.borderColor = "";

                let responseJson = {};

                responseJson.admissionNum = details.admissionNum;
                responseJson.docId = docId.value;

                serverRequest.open("POST" ,"http://localhost:8080/HosManSysServlet/AssignForDoctor");
                serverRequest.setRequestHeader("Content-Type" ,"application/json");
                serverRequest.send(JSON.stringify(responseJson));

                serverRequest.onreadystatechange = () =>
                {

                    if (serverRequest.readyState == 4)
                    {
                
                        let parsedJson = JSON.parse(serverRequest.responseText);
                        if (serverRequest.status == 400)
                        {
                            if (parsedJson.docId != undefined && parsedJson.docId.length != 0)
                            {
                                docIdWarn.innerText = parsedJson.docId;
                                docId.style.borderColor = "red";
                            }
                            if (parsedJson.Message != undefined && parsedJson.Message.length != 0)
                            {
                                alert(parsedJson.Message);
                            }
                        }
                        else if (serverRequest.status == 200)
                        {
                            popupBack.remove();
                            alert(parsedJson.Message);
                            getPatients(patiSearch, filter, sortby, list, popupBack, body);
                        }
                    }
                }
            }
            else
            {
                docIdWarn.innerText = "Please enter a doctor id";
                docId.style.borderColor = "red";
            }

        }

        cancel.onclick = () => {
            form.remove();
            popupBack.appendChild(detailsBox);
    
        }

}