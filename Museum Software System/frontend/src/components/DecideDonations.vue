<template>
    <v-card variant="tonal">
        <v-card-title>Pending Donations</v-card-title>
        <v-simple-table>
            <thead>
                <tr>
                    <th class="text-left">
                        #
                    </th>
                    <th class="text-left">
                        Artwork
                    </th>
                    <th class="text-left">
                        Donator
                    </th>
                    <th class="text-left"></th>
                    <th class="text-left"></th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="donation in pendingDonations" :key="donation.id">
                    <td>{{ donation.id }}</td>
                    <td>{{ donation.name }}</td>
                    <td>{{ donation.donator.emailAddress }}</td>
                    <td> <v-btn class="ma-2" color="success" @click="approveDonation(donation.id)">
                            Approve
                            <v-icon end icon="mdi-checkbox-marked-circle"></v-icon>
                        </v-btn>
                    </td>
                    <td> <v-btn class="ma-2" color="error" @click="rejectDonation(donation.id)">
                            Reject
                            <v-icon end icon="mdi-checkbox-marked-circle"></v-icon>
                        </v-btn>
                    </td>
                </tr>
            </tbody>
        </v-simple-table>
    </v-card>
</template>

<script>

import axios from 'axios';

export default {
    name: 'DecideDonations',

    components: {},

    data() {
        return {
            pendingDonations: [],
        }
    },

    methods: {
        getPendingDonations() {
            let options = {
                method: 'GET',
                url: `http://localhost:8090/donation/status/InReview`,
                headers: {
                    'Content-Type': 'application/json',
                },
            };

            axios.request(options)
                .then(response => response.data)
                .then(pendingDonations => {
                    this.pendingDonations = pendingDonations;
                    //console.log(pendingDonations)
                })
                .catch(err => console.error(err));
        },

        rejectDonation(id) {
            let options = {
                method: 'POST',
                url: `http://localhost:8090/donation/reject`,
                headers: {
                    'Content-Type': 'application/json',
                },
                data: {
                    id: id
                }
            };

            axios.request(options)
                .then(response => response.data)
                .then(() => {
                    this.getPendingDonations();       
                })
                .catch(err => console.error(err));
        },

        approveDonation(id) {
            let options = {
                method: 'POST',
                url: `http://localhost:8090/donation/approve`,
                headers: {
                    'Content-Type': 'application/json',
                },
                data: {
                    id: id
                }
            };

            axios.request(options)
                .then(response => response.data)
                .then(() => {
                    this.getPendingDonations();       
                })
                .catch(err => console.error(err));
        }
    },

    created() {
        this.getPendingDonations();
    }


};
</script>