import './App.css'
import {
    CssBaseline,
    ThemeProvider,
    createTheme,
} from "@mui/material";
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';

import darkScrollbar from '@mui/material/darkScrollbar';
import AppRouter from "./AppRouter.tsx";
import {QueryClient, QueryClientProvider} from "@tanstack/react-query";

const darkTheme = createTheme({
    palette: {
        mode: 'dark',
        background: {
            default: '#121212',
            paper: '#1e1e1e',
        },
    },
    components: {
        MuiCssBaseline: {
            styleOverrides: () => ({
                body: darkScrollbar(),
            }),
        },
    },
});
const queryClient = new QueryClient()

function App() {
    return (
        <QueryClientProvider client={queryClient}>
        <ThemeProvider theme={darkTheme}>
            <CssBaseline />
            <LocalizationProvider dateAdapter={AdapterDayjs}>
                <AppRouter />
            </LocalizationProvider>
        </ThemeProvider>
        </QueryClientProvider>
    )
}

export default App