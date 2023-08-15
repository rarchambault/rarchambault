<!-- This is the admin's main user portal containing the main software functionality-->
<template>
    <v-app>
        <v-app-bar app elevation="4"> <!-- Menu bar -->

            <v-toolbar-title>Museum Software System</v-toolbar-title>

            <v-spacer></v-spacer>

            <v-btn color="error" href="/login" text @click="logout()">Logout</v-btn>
        </v-app-bar>
        <v-main> <!-- Main section of webpage -->
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
                                    <DecideLoans />
                                </v-sheet>
                            </v-col>
                        </v-row>

                        <v-row no-gutters>
                            <v-col>
                                <v-sheet class="pa-2 ma-2">
                                    <DecideDonations />
                                </v-sheet>
                            </v-col>
                        </v-row>
                    </v-tab-item>
                    <v-tab-item>
                        <v-row no-gutters>
                            <v-col>
                                <v-sheet class="pa-2 ma-2">
                                    <ArtworksOverview />
                                </v-sheet>
                            </v-col>
                        </v-row>
                    </v-tab-item>
                    <v-tab-item v-if="isOwner()">
                        <v-row no-gutters>
                            <v-col>
                                <v-sheet class="pa-2 ma-2">
                                    <EmployeesOverview />
                                </v-sheet>
                            </v-col>
                        </v-row>
                    </v-tab-item>
                    <v-tab-item>
                        <v-row no-gutters>
                            <v-col>
                                <v-sheet class="pa-2 ma-2">
                                    <EmployeeShifts />
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
//necessary imports of components 
import ArtworksOverview from '../components/ArtworksOverview.vue';
import DecideLoans from '../components/DecideLoans.vue';
import DecideDonations from '../components/DecideDonations.vue';
import EmployeesOverview from '../components/EmployeesOverview.vue';
import EmployeeShifts from '../components/EmployeeShifts.vue';

export default {
    name: 'AdminPage',

    components: {
        DecideLoans,
        DecideDonations,
        ArtworksOverview,
        EmployeesOverview,
        EmployeeShifts
    },

    data() {
        return {
            tab: null,
            items: ['Pending Requests', 'Artworks', 'Employees', 'My Shifts'],
        }
    },

    methods: {
        isOwner() {
            if(sessionStorage.getItem('isOwner') === 'true') return true;
            return false;
        },
        //Ensures session data is not stored when user logs out
        logout() {
            sessionStorage.setItem("email", null)
            sessionStorage.setItem("museum", null)
            sessionStorage.setItem("isOwner", false)
            sessionStorage.setItem("isEmployee", false)
            sessionStorage.setItem("isLoggedIn", false)
        },
        //Returns visibility of item
        getVisibility(){
            this.items = this.items.filter(item => {
                if(item === 'Employees'){
                    if(sessionStorage.getItem("isOwner") === "false") return false;
                }

                if(item === 'My Shifts'){
                    if(sessionStorage.getItem("isOwner") === "true") return false;
                }   
                
                return true;             
            })
        }
    },
    
    beforeMount(){
        this.getVisibility();
    }
};
</script>