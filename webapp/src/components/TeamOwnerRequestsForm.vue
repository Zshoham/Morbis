<template>
  <v-card raised class="mx-auto my-auto" max-width="500">
    <div align="center" dark class="primary mb-10 py-3 white--text">
      <h1>League's Options</h1>
    </div>

    <div align="center">
      <v-select
        v-model="selectedRequest"
        :items="requests"
        item-text="requestedTeamName"
        label="Request"
        class="mx-5"
        outlined
      ></v-select>

      <v-btn color="success" block @click="ApproveRequest">Approve</v-btn>
      <v-btn color="error" block @click="DenyRequest">Deny</v-btn>
    </div>
    <v-spacer></v-spacer>
  </v-card>
</template>

<script>
export default {
  name: "TeamOwnerRequestsForm",
  mounted() {
    this.getRequestsFromServer();
  },
  data: () => ({
    selectedRequest: "",
    requests: [],
  }),
  methods: {
    ApproveRequest() {
      let params = {
          memberID: this.requests.find(request => request.requestedTeamName == this.selectedRequest).requestingMemberID,
          approved: true
        }
        let url = new URL("http://localhost:8081/api/association-rep/handle-team-request");
        url.search = new URLSearchParams(params).toString();
        fetch(url, {
        method: "POST",
        headers: {
           "Content-Type": "application/json",
            'authorization': this.$root.userToken
        },
      })
        .then(response => {
          if (response.ok) {
            alert(response.status + ": server approved successfully");            
            this.getRequestsFromServer();
          } else {
            response.json().then(json => {
                alert(response.status + ": " + json.message);
            });
          }
        })
        .catch(err => console.error(err));
    },
    DenyRequest() {
        let params = {
          memberID: this.requests.find(request => request.requestedTeamName == this.selectedRequest).requestingMemberID,
          approved: false  
        }
        let url = new URL("http://localhost:8081/api/association-rep/handle-team-request");
        url.search = new URLSearchParams(params).toString();
        fetch(url, {
        method: "POST",
        headers: {
           "Content-Type": "application/json",
            'authorization': this.$root.userToken
        },
      })
        .then(response => {
          if (response.ok) {
            alert(response.status + ": server denied successfully");
            this.getRequestsFromServer();
          } else {
            response.json().then(json => {
                alert(response.status + ": " + json.message);
            });
          }
        })
        .catch(err => console.error(err));
    },
    getRequestsFromServer() {
      fetch("http://localhost:8081/api/association-rep/pending-team-requests", {
        method: "GET",
        headers: {
          'authorization': this.$root.userToken
        }
      })
        .then(response => {
          if (response.ok) {
            response.json().then(json => {
              this.requests = json;
            });
          } else {
            alert(
              "Server returned " + response.status + " : " + response.statusText
            );
          }
        })
        .catch(err => console.error(err));
    },
  }
};
</script>

<style scoped>
</style>