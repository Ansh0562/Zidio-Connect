const sign_in_btn = document.querySelector("#sign-in-btn");
const sign_up_btn = document.querySelector("#sign-up-btn");
const container = document.querySelector(".container");
const roleSelect = document.querySelector("#role");
const studentFields = document.querySelector("#student-fields");
const recruiterFields = document.querySelector("#recruiter-fields");
const adminFields = document.querySelector("#admin-fields");

// Toggle between sign in and sign up forms
sign_up_btn.addEventListener("click", () => {
    container.classList.add("sign-up-mode");
});

sign_in_btn.addEventListener("click", () => {
    container.classList.remove("sign-up-mode");
});

// Handle role selection and show/hide relevant fields
roleSelect.addEventListener("change", () => {
    const selectedRole = roleSelect.value;
    
    // Hide all role-specific fields first
    studentFields.style.display = "none";
    recruiterFields.style.display = "none";
    adminFields.style.display = "none";
    
    // Show fields based on selected role
    switch(selectedRole) {
        case "STUDENT":
            studentFields.style.display = "block";
            break;
        case "RECRUITER":
            recruiterFields.style.display = "block";
            break;
        case "ADMIN":
            adminFields.style.display = "block";
            break;
    }
});

// Handle form submissions
document.querySelector(".sign-in-form").addEventListener("submit", async (e) => {
    e.preventDefault();
    const email = e.target.querySelector('input[type="email"]').value;
    const password = e.target.querySelector('input[type="password"]').value;

    console.log('Login attempt for email:', email);

    try {
        const response = await fetch('/api/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ email, password })
        });

        console.log('Login response status:', response.status);

        if (response.ok) {
            const data = await response.json();
            console.log('Login response data:', data);
            
            // Store the token
            localStorage.setItem('token', data.token);
            console.log('Token stored:', data.token);
            
            // Redirect to dashboard
            window.location.href = '/dashboard.html';
        } else {
            const errorData = await response.json();
            console.log('Login error:', errorData);
            alert('Login failed. Please check your credentials.');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('An error occurred during login.');
    }
});

document.querySelector(".sign-up-form").addEventListener("submit", async (e) => {
    e.preventDefault();
    const formData = {
        name: e.target.querySelector('input[type="text"]').value,
        email: e.target.querySelector('input[type="email"]').value,
        password: e.target.querySelector('input[type="password"]').value,
        role: roleSelect.value
    };

    // Add role-specific data
    switch(formData.role) {
        case "STUDENT":
            formData.education = studentFields.querySelector('input[placeholder="Education"]').value;
            formData.skills = studentFields.querySelector('input[placeholder="Skills"]').value;
            break;
        case "RECRUITER":
            formData.companyName = recruiterFields.querySelector('input[placeholder="Company Name"]').value;
            formData.designation = recruiterFields.querySelector('input[placeholder="Designation"]').value;
            break;
        case "ADMIN":
            formData.department = adminFields.querySelector('input[placeholder="Department"]').value;
            break;
    }

    console.log('Registration attempt:', formData);

    try {
        const response = await fetch('/api/auth/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(formData)
        });

        console.log('Registration response status:', response.status);

        if (response.ok) {
            alert('Registration successful! Please login.');
            container.classList.remove("sign-up-mode");
        } else {
            const error = await response.json();
            console.log('Registration error:', error);
            alert(error.message || 'Registration failed. Please try again.');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('An error occurred during registration.');
    }
}); 