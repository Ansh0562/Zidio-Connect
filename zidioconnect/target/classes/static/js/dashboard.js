// Check if user is logged in
document.addEventListener('DOMContentLoaded', () => {
    const token = localStorage.getItem('token');
    console.log('Token from localStorage:', token);
    
    if (!token) {
        console.log('No token found, redirecting to login');
        window.location.href = '/index.html';
        return;
    }

    // Fetch user data
    fetchUserData();
    
    // Initialize navigation
    initializeNavigation();
    
    // Initialize job creation functionality
    initializeJobCreation();
});

// Fetch user data from the server
async function fetchUserData() {
    try {
        const token = localStorage.getItem('token');
        console.log('Making request with token:', token);
        
        const response = await fetch('/api/user/profile', {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        console.log('Response status:', response.status);
        console.log('Response headers:', response.headers);

        if (response.ok) {
            const userData = await response.json();
            console.log('User data received:', userData);
            updateDashboard(userData);
        } else {
            console.log('Response not ok, status:', response.status);
            const errorText = await response.text();
            console.log('Error response:', errorText);
            
            // If token is invalid, redirect to login
            localStorage.removeItem('token');
            window.location.href = '/index.html';
        }
    } catch (error) {
        console.error('Error fetching user data:', error);
    }
}

// Update dashboard with user data
function updateDashboard(userData) {
    document.getElementById('user-name').textContent = userData.name;
    document.getElementById('user-role').textContent = userData.role;

    // Show create job button for recruiters and admins
    const createJobBtn = document.getElementById('create-job-btn');
    if (userData.role === 'RECRUITER' || userData.role === 'ADMIN') {
        createJobBtn.style.display = 'block';
    }

    // Update stats based on user role
    updateStats(userData);
}

// Update stats based on user role
function updateStats(userData) {
    // This would be populated with real data from the server
    const stats = {
        jobs: 0,
        applications: 0,
        messages: 0
    };

    // Update stats display
    document.querySelector('.stat-card:nth-child(1) p').textContent = stats.jobs;
    document.querySelector('.stat-card:nth-child(2) p').textContent = stats.applications;
    document.querySelector('.stat-card:nth-child(3) p').textContent = stats.messages;
}

// Initialize navigation
function initializeNavigation() {
    document.querySelectorAll('.nav-links a').forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            
            // Remove active class from all links
            document.querySelectorAll('.nav-links li').forEach(li => {
                li.classList.remove('active');
            });

            // Add active class to clicked link
            link.parentElement.classList.add('active');

            // Show corresponding content
            const section = link.getAttribute('href').substring(1);
            showSection(section);
        });
    });
}

// Show specific section content
function showSection(section) {
    // Hide all content sections
    document.querySelectorAll('.content-section, .dashboard-content').forEach(content => {
        content.style.display = 'none';
    });

    // Show the selected section
    switch(section) {
        case 'dashboard':
            document.getElementById('dashboard-content').style.display = 'block';
            break;
        case 'jobs':
            document.getElementById('jobs-content').style.display = 'block';
            loadJobListings();
            break;
        default:
            document.getElementById('dashboard-content').style.display = 'block';
    }
}

// Initialize job creation functionality
function initializeJobCreation() {
    const createJobBtn = document.getElementById('create-job-btn');
    const jobFormContainer = document.getElementById('job-form-container');
    const cancelJobBtn = document.getElementById('cancel-job-btn');
    const jobForm = document.getElementById('job-creation-form');

    // Show job creation form
    createJobBtn.addEventListener('click', () => {
        jobFormContainer.style.display = 'block';
        document.getElementById('job-listings').style.display = 'none';
    });

    // Hide job creation form
    cancelJobBtn.addEventListener('click', () => {
        jobFormContainer.style.display = 'none';
        document.getElementById('job-listings').style.display = 'block';
        jobForm.reset();
    });

    // Handle job form submission
    jobForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const formData = new FormData(jobForm);
        const jobData = {
            title: formData.get('title'),
            description: formData.get('description'),
            type: formData.get('type'),
            location: formData.get('location'),
            salary: formData.get('salary'),
            remote: formData.get('remote') === 'true',
            requirements: formData.get('requirements')
        };

        try {
            const token = localStorage.getItem('token');
            const response = await fetch('/api/joblistings', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(jobData)
            });

            console.log('Job creation response status:', response.status);

            if (response.ok) {
                const createdJob = await response.json();
                console.log('Job created successfully:', createdJob);
                alert('Job listing created successfully!');
                
                // Reset form and show job listings
                jobForm.reset();
                jobFormContainer.style.display = 'none';
                document.getElementById('job-listings').style.display = 'block';
                loadJobListings();
            } else {
                const errorData = await response.json();
                console.error('Job creation error:', errorData);
                alert('Error creating job listing: ' + (errorData.message || 'Unknown error'));
            }
        } catch (error) {
            console.error('Error creating job:', error);
            alert('An error occurred while creating the job listing.');
        }
    });
}

// Load job listings
async function loadJobListings() {
    try {
        const token = localStorage.getItem('token');
        const response = await fetch('/api/joblistings', {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (response.ok) {
            const jobs = await response.json();
            displayJobListings(jobs);
        } else {
            console.error('Error loading job listings:', response.status);
        }
    } catch (error) {
        console.error('Error loading job listings:', error);
    }
}

// Display job listings
function displayJobListings(jobs) {
    const jobsList = document.getElementById('jobs-list');
    
    if (jobs.length === 0) {
        jobsList.innerHTML = '<p class="no-jobs">No job listings found.</p>';
        return;
    }

    jobsList.innerHTML = jobs.map(job => `
        <div class="job-card">
            <h4>${job.title}</h4>
            <p class="job-type">${job.type}</p>
            <p class="job-location">${job.location}</p>
            <p class="job-description">${job.description.substring(0, 100)}...</p>
            <div class="job-actions">
                <button class="btn-secondary" onclick="editJob(${job.id})">Edit</button>
                <button class="btn-danger" onclick="deleteJob(${job.id})">Delete</button>
            </div>
        </div>
    `).join('');
}

// Handle logout
document.getElementById('logout-btn').addEventListener('click', (e) => {
    e.preventDefault();
    localStorage.removeItem('token');
    window.location.href = '/index.html';
}); 