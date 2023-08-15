<!-- This is the main visitor page. This is where visitors to the website can use the functionality of the software system-->
<template>
    <v-app>
        <v-app-bar app elevation="4">

            <v-toolbar-title>Museum Software System</v-toolbar-title> <!-- Toolbar -->

            <v-spacer></v-spacer>

            <v-btn color="error" href="/login"  text @click="logout">
                Logout
            </v-btn>

        </v-app-bar>

        <v-main> <!-- Main section of Website-->
            <v-container class="bg-surface-variant">
                <v-tabs v-model="tab">
                    <v-tab v-for="item in items" :key="item">
                        {{ item }}
                    </v-tab>
                </v-tabs>
                <v-tabs-items v-model="tab">
                    <v-tab-item>
                        <v-row no-gutters>
                            <v-col>
                                <v-sheet class="pa-2 ma-2">
                                    <ArtworkTable />
                                </v-sheet>
                            </v-col>
                        </v-row>
                    </v-tab-item>
                    <v-tab-item>
                        <v-row no-gutters>
                            <v-col>
                                <v-sheet class="pa-2 ma-2">
                                    <PassTable />
                                </v-sheet>
                            </v-col>
                        </v-row>
                    </v-tab-item>
                    <v-tab-item>
                        <v-row no-gutters>
                            <v-col>
                                <v-sheet class="pa-2 ma-2">
                                    <DonationTable />
                                </v-sheet>
                            </v-col>
                        </v-row>
                    </v-tab-item>
                    <v-tab-item>
                        <v-row no-gutters>
                            <v-col>
                                <v-sheet class="pa-2 ma-2">
                                    <LoanTable />
                                </v-sheet>
                            </v-col>
                        </v-row>
                    </v-tab-item>
                </v-tabs-items>
            </v-container>
        </v-main>
    </v-app>
</template>

<script>
//necessary imports
//import axios from 'axios'
import PassTable from '../components/PassTable.vue'
import DonationTable from '../components/DonationTable.vue'
import LoanTable from '../components/LoanTable.vue'
import ArtworkTable from '../components/ArtworkTable.vue'
export default {
    name: 'VisitorPage',

    components: {ArtworkTable, PassTable, DonationTable, LoanTable},

    data: () => ({
        tab: null,
        items: ['Artworks', 'Passes', 'Donations', 'Loans']
    }),

    methods: {
        // Ensures session information is cleared when a session is ended 
        logout() {
            sessionStorage.setItem("email", "")
            sessionStorage.setItem("museum", "")
        }
    }
};
</script>