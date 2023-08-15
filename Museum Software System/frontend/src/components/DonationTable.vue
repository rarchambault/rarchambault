<template>
    <v-app align="center">
        <v-card>
            <v-list-item three-line>
                <v-list-item-content>
                    <v-card-title>My Donations</v-card-title>
                </v-list-item-content>
                <AddDonationForm />
            </v-list-item>
            <v-simple-table>
                <thead>
                    <tr>
                        <th class="text-left">
                            ID
                        </th>
                        <th class="text-left">
                            Name
                        </th>
                        <th class="text-left">
                            Request Date
                        </th>
                        <th class="text-left">
                            Approval Status
                        </th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="donation in donations" :key="donation.id">
                        <td>{{ donation.id }}</td>
                        <td>{{ donation.name }}</td>
                        <td>{{ donation.requestDate }}</td>
                        <td>{{ donation.status }}</td>
                    </tr>
                </tbody>
            </v-simple-table>
        </v-card>
    </v-app>
</template>

<script>
import axios from 'axios'
import AddDonationForm from '../components/AddDonationForm.vue'

var backendUrl = 'http://localhost:8090'

// Instantiate axios client
var AXIOS = axios.create({
    baseURL: backendUrl,
})

export default {
    name: 'DonationTable',

    components: { AddDonationForm },

    data() {
        return {
            donations: [],
        };
    },

    methods: {
        // Gets all donations for the visitor currently logged in
        getAllDonations() {
            if (sessionStorage.getItem("email") == "")
            {
                alert("Error: You need to login to view and submit donation requests.")
                window.location = "/login"
                return
            }

            AXIOS
                .get('/donation/donatorEmailAddress/' + sessionStorage.getItem("email"))
                .then(res => res.data)
                .then(response => {
                    //console.log(response);
                    if (response === 'Donations not found.') return;
                    this.donations = response;
                })
                .catch(error => {
                    console.log(error)
                    alert("Oops! An error occurred: " + error)
                })
        }
    },

    created() {
        this.getAllDonations()
    }
}

</script>