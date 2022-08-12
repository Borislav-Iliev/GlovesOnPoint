let loadProfilesBtn = document.getElementById('loadProfiles');

loadProfilesBtn.addEventListener('click', onLoadProfiles)

function onLoadProfiles(event) {
    let requestOptions = {
        method: 'GET', redirect: 'follow'
    };

    let profilesContainer = document.getElementById('profiles-container');
    profilesContainer.innerHTML = '';

    fetch("http://localhost:8080/users/profile/top", requestOptions)
        .then(response => response.json())
        .then(json => json.forEach(profile => {

            let row = document.createElement('tr');

            let usernameCol = document.createElement('td');
            let firstNameCol = document.createElement('td');
            let lastNameCol = document.createElement('td');
            let postsCol = document.createElement('td');
            let commentsCol = document.createElement('td');

            usernameCol.textContent = profile.username;
            firstNameCol.textContent = profile.firstName;
            lastNameCol.textContent = profile.lastName;
            postsCol.textContent = profile.posts.size;
            commentsCol.textContent = profile.comments.size;

            row.appendChild(usernameCol);
            row.appendChild(firstNameCol);
            row.appendChild(lastNameCol);
            row.appendChild(postsCol);
            row.appendChild(commentsCol);

            profilesContainer.appendChild(row);
        }))
        .catch(error => console.log('error', error));
}