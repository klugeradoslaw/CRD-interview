import {Box, Grid, Typography,} from "@mui/material";
import Card from "../components/Card";
import RentalDatesForm from "../components/RentalDatesForm.tsx";

const Home = () => {
    const imageUrl = "https://media.cntraveler.com/photos/66d0cab3759c2a9f11040cae/master/w_1600%2Cc_limit/GettyImages-1196654130.jpg";

    return (
        <Box
            sx={{
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                minHeight: '100vh',
                backgroundColor: '#121212',
                p: 2,
            }}
        >
            <Card
                sx={{
                    width: {xs: '95vw', md: '1100px'},
                    minHeight: '450px',
                    maxHeight: '550px'
                }}>
                <Grid
                    sx={{
                        display: 'flex',
                        flexDirection: 'column',
                        flex: 1,
                        p: 4
                    }}
                >
                    <Typography variant="h4" sx={{mb: 3, fontWeight: 600, color: '#f5f5f5', textAlign: 'center'}}>
                        CDR - Car Rental
                    </Typography>
                    <RentalDatesForm direction="column"/>
                </Grid>

                <Box
                    sx={{
                        flex: 1,
                        display: 'flex',
                        borderBottomRightRadius: 12,
                        borderTopRightRadius: 12,
                        overflow: 'hidden',
                        backgroundColor: '#161616',
                    }}
                >
                    <Box
                        component="img"
                        src={imageUrl}
                        sx={{
                            width: '100%',
                            height: '100%',
                            objectFit: 'cover',
                            display: 'block',
                        }}
                    />
                </Box>
            </Card>
        </Box>
    );
};

export default Home;