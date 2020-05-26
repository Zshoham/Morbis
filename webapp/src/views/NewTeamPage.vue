<template>
  <div class="NewTeamPage">
    <NewTeamForm/>
  </div>
</template>

<script>
// @ is an alias to /src
import NewTeamForm from '@/components/team_owner_components/NewTeamForm.vue'

export default {
  name: 'NewTeamPage',
  components: {
    NewTeamForm
  }
}
</script>
